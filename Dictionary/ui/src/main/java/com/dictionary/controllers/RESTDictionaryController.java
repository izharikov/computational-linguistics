package com.dictionary.controllers;

import com.db.entity.DictWord;
import com.db.factory.DictFactory;
import com.dictionary.DictionaryUiApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by igor on 4.10.16.
 */
@RestController
@RequestMapping("/rest/dictionary")
public class RESTDictionaryController {
    private static final int PAGE_LENGTH = 100;
    private static DictFactory dictFactory = DictionaryUiApplication.DICT_FACTORY;

    @RequestMapping("/all-words")
    public Map<String, Object> getAllWords(@RequestParam(value = "page", defaultValue = "0") Integer page){
        System.out.println("all words");
        Map<String, Object> result = new HashMap<>();
        int startPosition = PAGE_LENGTH * page;
        List<DictWord> words = dictFactory.getWordsFromPage(startPosition, PAGE_LENGTH);
        int s = dictFactory.getCountOfRecords();
        int l = PAGE_LENGTH;
        result.put("pageCount", s % l == 0 ? s / l : s / l + 1);
        result.put("words", words);
        return result;
    }

    @RequestMapping("/search")
    public Map<String, Object> doSearch(@RequestParam(value = "term", required = true) String pTerm,
                                               @RequestParam(value = "page", required = false, defaultValue = "0")Integer pPage){
        Map<String, Object> result = new HashMap<>();
        int startPosition = PAGE_LENGTH * pPage;
        List<DictWord> words = dictFactory.findWordsByTerm(pTerm, startPosition, PAGE_LENGTH);
        int s = dictFactory.getCountOfSearchPages(pTerm);
        int l = PAGE_LENGTH;
        result.put("pageCount", dictFactory.getCountOfSearchPages(pTerm));
        result.put("searchResult", words);
        result.put("allCount", dictFactory.getCountOfSearchResults(pTerm));
        return result;
    }

//    @RequestMapping("/results")
//    public Map<String, Object> getResults(@RequestParam(value = "term", required = false, defaultValue = "*") String pTerm,
//                                                 @RequestParam(value = "page", required = false, defaultValue = "0")Integer pPage,
//                                                 @RequestParam(value = "sortBy", required = false, defaultValue = "none")String pSortBy){
//        Map<String, Object> result = new HashMap<>();
//        int startPosition = PAGE_LENGTH * pPage;
//        List<DictWord> words = dictFactory.getResults(pTerm, startPosition, PAGE_LENGTH, pSortBy);
//        result.put("pageCount", dictFactory.getCountOfSearchPages(pTerm));
//        result.put("result", words);
//        result.put("allCount", dictFactory.getCountOfSearchResults(pTerm));
//        System.out.println("RESULT: " + result);
//        return result;
//    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public boolean updateWord(@RequestParam(value = "id" ) int pId,
                                  @RequestParam(value = "word") String pWord,
                                  @RequestParam(value = "count") int pCount){
        return dictFactory.updateWord(pId, pWord, pCount);
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public static boolean addWord(  @RequestParam(value = "word") String pWord,
                                     @RequestParam(value = "count") int pCount){
        return dictFactory.addNewWord(pWord, pCount);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public static boolean removeWord(@RequestParam(value = "id" ) int pId){
        return dictFactory.deleteWord(pId);
    }
}
