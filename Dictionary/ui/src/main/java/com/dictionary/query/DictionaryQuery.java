package com.dictionary.query;

import com.db.entity.DictWord;
import com.dictionary.query.result.DQueryResult;
import com.dictionary.query.sort.SortOption;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Ihar Zharykau
 */
public interface DictionaryQuery {
    int PAGE_SIZE = 100;
    DictionaryQuery setSession(Session session);
    DictionaryQuery setSearchTerm(String term);
    DictionaryQuery setPageNo(int pageNo);
    DictionaryQuery setSortOption(SortOption sortOption);
    DQueryResult execute();
}
