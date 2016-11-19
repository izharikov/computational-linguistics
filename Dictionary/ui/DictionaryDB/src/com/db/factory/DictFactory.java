package com.db.factory;

import com.db.entity.DictWord;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.*;

/**
 * Created by igor on 4.10.16.
 */
public class DictFactory {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;


    public DictFactory() {
        int count = getCountOfRecords();
        searchInfo.put("*", Arrays.asList(count, count / 100 + 1));
    }

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public List<DictWord> getAllWords() {
        final Session session = getSession();
        List<DictWord> dictWords = new ArrayList<>();
        try {
            final Query query = session.createQuery("from " + DictWord.class.getSimpleName());
            dictWords = query.list();
        } finally {
            session.close();
        }
        return dictWords;
    }

    public List<DictWord> getWordsFromPage(int pStartPosition, int pPageLength) {
        final Session session = getSession();
        List<DictWord> dictWords = new ArrayList<>();
        try {
            final Query query = session.createQuery("from " + DictWord.class.getSimpleName());
            query.setFirstResult(pStartPosition);
            query.setMaxResults(pPageLength);
            dictWords = query.list();
        } finally {
            session.close();
        }
        return dictWords;
    }

    public int getCountOfRecords() {
        int count = ((Long) getSession().createQuery("select count(*) from " + DictWord.class.getSimpleName()).uniqueResult()).intValue();
        return count;
    }

    public int getCountOfSearchResults(String pTerm) {
        return searchInfo.get(pTerm).get(0);
    }

    /**
     * list : count of search results, count of pages
     */
    private Map<String, List<Integer>> searchInfo = new HashMap<>();

    public List<DictWord> findWordsByTerm(String pTerm, int pStartPosition, int pPageLength) {
        List<DictWord> result = new ArrayList<>();
        final Session session = getSession();
        try {
            Query query = session.createQuery("from " + DictWord.class.getSimpleName() + " where word like concat('%', :term, '%') ");
            query.setParameter("term", pTerm);
            if (!searchInfo.containsKey(pTerm)) {
                List<Integer> searchInfoList = new ArrayList<>();
                searchInfoList.add(query.list().size());
                int s = searchInfoList.get(0);
                int l = pPageLength;
                searchInfoList.add(s % l == 0 ? s / l : s / l + 1);
                searchInfo.put(pTerm, searchInfoList);
            }
            query.setFirstResult(pStartPosition);
            query.setMaxResults(pPageLength);
            result = query.list();
        } finally {
            session.close();
        }
        return result;
    }

    public List<DictWord> getResults(String pTerm, int pStartPosition, int pPageLength, String pOrderBy) {
        List<DictWord> result = new ArrayList<>();
        final Session session = getSession();
        try {
            Query query = createHQLQuery(session, pTerm, pOrderBy);
            if (!searchInfo.containsKey(pTerm)) {
                List<Integer> searchInfoList = new ArrayList<>();
                searchInfoList.add(query.list().size());
                int s = searchInfoList.get(0);
                int l = pPageLength;
                searchInfoList.add(s % l == 0 ? s / l : s / l + 1);
                searchInfo.put(pTerm, searchInfoList);
            }
            query.setMaxResults(pPageLength);
            query.setFirstResult(pStartPosition);
            result = query.list();
        } finally {
            session.close();
        }
        return result;
    }

    public static Query createHQLQuery(Session pSession, String pTerm, String pOrderBy) {
        String queryStr = "from " + DictWord.class.getSimpleName();
        if (!pTerm.equals("*")) {
            queryStr += " where word like concat('%', :term, '%')";
        }
        if (!pOrderBy.equals("none")) {
            if (pOrderBy.equalsIgnoreCase("wordASC")) {
                queryStr += " order by word asc";
            } else if (pOrderBy.equalsIgnoreCase("wordDESC")) {
                queryStr += " order by word desc";
            } else if (pOrderBy.equalsIgnoreCase("countASC")) {
                queryStr += " order by wordCount asc";
            } else if (pOrderBy.equalsIgnoreCase("countDESC")) {
                queryStr += " order by wordCount desc";
            }
        }
        Query q = pSession.createQuery(queryStr);
        if (!pTerm.equals("*")) {
            q.setParameter("term", pTerm);
        }
        System.out.println(queryStr);
        return q;
    }

    public boolean addNewWord(String pWord, int pCount) {
        boolean success = true;
        Session session = getSession();
        try {
            int index = checkWordDuplicate(session, pWord, -1);
            success = index == -1;
            if (success) {
                DictWord dictWord = new DictWord();
                dictWord.setWord(pWord);
                dictWord.setWordCount(pCount);
                Transaction tr = session.beginTransaction();
                session.save(dictWord);
                tr.commit();
            }
        } finally {
            session.close();
        }
        return success;
    }

    private boolean updateWord(int pId, String pWord, int pCount, Session pSession){
        boolean success = true;
        Session session = pSession;
        DictWord dictWord = null;
        try {
            int updId = checkWordDuplicate(session, pWord, pId);
            System.out.println("ID: " + updId);
            if (updId == pId || updId == -1) {
                dictWord = (DictWord) session.get(DictWord.class, pId);
                if ( dictWord != null) {
                    dictWord.setWordCount(pCount);
                    dictWord.setWord(pWord);
                    Transaction tr = session.beginTransaction();
                    session.update(dictWord);
                    tr.commit();
                }
                else{
                    success = false;
                }
            }
            else{
                System.out.println("ID: " + updId);
                dictWord = (DictWord) session.get(DictWord.class, updId);
                deleteWord(pId, session);
                return updateWord(updId, pWord, dictWord.getWordCount() + pCount, session);
            }
        } finally {
            //session.close();
        }
        return success;
    }

    public boolean updateWord(int pId, String pWord, int pCount) {
        Session session = getSession();
        boolean res = updateWord(pId, pWord, pCount, session);
        session.close();
        return res;
    }

    public boolean deleteWord(int pId){
        Session session = getSession();
        boolean res = deleteWord(pId, session);
        session.close();
        return res;
    }

    private boolean deleteWord(int pId, Session pSession){
        boolean success = true;
        Session session = pSession;
        try {
            DictWord dictWord = (DictWord) session.get(DictWord.class, pId);
            if ( dictWord != null) {
                Transaction tr = session.beginTransaction();
                session.delete(dictWord);
                tr.commit();
            }
            else{
                success = false;
            }
        }
        finally {

        }
        return success;
    }

    /**
     * return id of dictWord with word pWord
     * @param pSession
     * @param pWord
     * @param pId
     * @return
     */
    protected static int checkWordDuplicate(Session pSession, String pWord, int pId) {
        boolean duplicate = false;
        Query query = pSession.createQuery("select id FROM " + DictWord.class.getSimpleName() + " where word = :word");
        query.setParameter("word", pWord);
        List<Integer> res = query.list();
        if ( res.size() > 0){
            return res.get(0);
        }
        else{
            return -1;
        }
    }

    public Integer getCountOfSearchPages(String pTerm) {
        return searchInfo.get(pTerm).get(1);
    }

    public static void main(String... args){
        Session s = getSession();
        DictWord dictWord = (DictWord) s.get(DictWord.class, 12);
        System.out.println(dictWord);
    }
}
