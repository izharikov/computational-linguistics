package com.dictionary.utils;

import com.db.entity.DictWord;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class BaseUtils {
    public static List<Integer> fromJSON(String jsonString){
        JSONArray jsonArray = new JSONArray(jsonString);
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            ids.add(Integer.parseInt(jsonArray.get(i).toString()));
        }
        System.out.println("IDS : " + ids);
        return ids;
    }


    public static DictWord getWord(int id, String word, int count, String posIds){
        List<Integer> podIdsList = BaseUtils.fromJSON(posIds);
        return null;
    }
}
