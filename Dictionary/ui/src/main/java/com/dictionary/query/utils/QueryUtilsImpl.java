package com.dictionary.query.utils;

import com.db.entity.DictWord;
import org.hibernate.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ihar Zharykau
 */
public class QueryUtilsImpl implements QueryUtils {

    @Override
    @SuppressWarnings("unchecked")
    public Map wrapQueryResult(List<DictWord> result) {
        Map map = new HashMap();
        map.put(RESULT, result);

//        result.put()
        return map;

    }
}
