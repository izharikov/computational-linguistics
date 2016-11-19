package com.dictionary.query;

import com.db.entity.DictWord;
import com.dictionary.query.result.DQueryResult;
import com.dictionary.query.sort.SortOption;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class DictionaryQueryImpl implements DictionaryQuery {
    private Session session;
    private String queryStr;
    private int pageNo = 0;

    public DictionaryQueryImpl() {
        queryStr = "from " + DictWord.class.getSimpleName() + " ";
    }

    @Override
    public DictionaryQuery setSession(Session session) {
        this.session = session;
        return this;
    }

    @Override
    public DictionaryQuery setSearchTerm(String term) {
        if (!term.equals("*")) {
            queryStr += " where word like '%" + term + "%' ";
        }
        return this;
    }

    @Override
    public DictionaryQuery setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    @Override
    public DictionaryQuery setSortOption(SortOption sortOption) {
        queryStr += sortOption.toString();
        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public DQueryResult execute() {
        System.out.println(queryStr);
        Query query = session.createQuery(queryStr);
        int allCount = ((Long) session.createQuery("select count(*) " +  queryStr).uniqueResult()).intValue();
        query.setMaxResults(PAGE_SIZE);
        query.setFirstResult(pageNo * PAGE_SIZE);
        List<DictWord> words = query.list();
        int pageCount = allCount / PAGE_SIZE;
        if ( allCount % PAGE_SIZE != 0){
            pageCount++;
        }
        return new DQueryResult(pageNo, words, allCount, pageCount);
    }
}
