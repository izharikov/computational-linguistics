package com.dictionary.query.utils;

import com.db.entity.DictWord;
import com.dictionary.query.DictionaryQuery;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Ihar Zharykau
 */
public interface QueryUtils {
    String RESULT = "result";
    String PAGE_COUNT = "pageCount";

    Map wrapQueryResult(List<DictWord> result);
}
