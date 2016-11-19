package com.db.utils;

import com.db.entity.DictWord;
import com.db.factory.DictFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class DictionaryUtils {
    private static Session session;

    static {
        session = DictFactory.getSession();
    }


    public static void saveAll(Collection<DictWord> words){
        Transaction tr = startTransaction();
        for(DictWord dictWord :words){
            if ( dictWord.getPosTags() != null && dictWord.getPosTags().size() > 0) {
                saveOrUpdate(dictWord);
            }
        }
        tr.commit();
    }

    public static void saveOrUpdate(DictWord dictWord){
        Object obj = session.merge(dictWord);
        session.saveOrUpdate(obj);
    }

    public static Transaction startTransaction(){
        return session.beginTransaction();
    }

    public static void reopenSession(){
        session.close();
        session = DictFactory.getSession();
    }
}
