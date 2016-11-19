package com.dictionary.controllers;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.dictionary.query.DictionaryQuery;
import com.dictionary.query.QueryConfiguration;
import com.dictionary.query.result.DQueryResult;
import com.dictionary.query.sort.SortOption;
import com.dictionary.repository.DictionaryRepository;
import com.dictionary.utils.BaseUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
@RequestMapping("/rest/dictionary/")
@RestController
public class RESTTestController {
    public static final int PAGE_SIZE = 100;
    @Autowired
    DictionaryRepository dictionaryRepository;

    private DictionaryQuery dictionaryQuery;

    @RequestMapping("results")
    public DQueryResult doResults(@RequestParam(value = "term", required = false, defaultValue = "*") String pTerm,
                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer pPage,
                                  @RequestParam(value = "sortBy", required = false, defaultValue = "none") String pSortBy,
                                  @RequestParam(value = "pos", required = false, defaultValue = "") String tags) {
        System.out.println("Tags " + tags);
        dictionaryQuery = new QueryConfiguration().dictionaryQuery();
        SortOption sortOption = SortOption.byString(pSortBy);
        return dictionaryQuery.setPageNo(pPage).
                setSearchTerm(pTerm).
                setSortOption(sortOption).
                execute();
    }

    @RequestMapping("/a")
    public DictWord get(@RequestParam(value = "word", required = false, defaultValue = "") String word) {
        return dictionaryRepository.findByWord(word);
    }

    @RequestMapping("/b")
    public Page<DictWord> getB() {
        return dictionaryRepository.findAll(new PageRequest(0, 10));
    }

    @RequestMapping("/jpa/results")
    public Page<DictWord> like(@RequestParam(value = "term", required = false, defaultValue = "") String pTerm,
                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer pPage,
                               @RequestParam(value = "sortBy", required = false, defaultValue = "none") String pSortBy,
                               @RequestParam(value = "pos", required = false, defaultValue = "[]") String posIds) {
        List<Integer> posTags = BaseUtils.fromJSON(posIds);
        if (posTags.size() > 0) {
            return dictionaryRepository.findByWordLikeAndPosTags_IdIn("%" + pTerm + "%", posTags, createPageRequest(pPage, PAGE_SIZE, pSortBy));
        }
        else{
            return dictionaryRepository.findByWordLike("%" + pTerm + "%", createPageRequest(pPage, PAGE_SIZE, pSortBy));
        }
    }


    private static Pageable createPageRequest(int pageNo, int pageSize, String sortBy) {
        Sort sort = getSort(sortBy);
        if (sort != null) {
            return new PageRequest(pageNo, pageSize, sort);
        }
        return new PageRequest(pageNo, pageSize);
    }

    private static Sort getSort(String sort) {
        switch (sort) {
            case "wordASC":
                return new Sort(Sort.Direction.ASC, "word");
            case "wordDESC":
                return new Sort(Sort.Direction.DESC, "word");
            case "countASC":
                return new Sort(Sort.Direction.ASC, "wordCount");
            case "countDESC":
                return new Sort(Sort.Direction.DESC, "wordCount");
            case "posASC":
                return new Sort(Sort.Direction.ASC, "posTags");
            case "posDESC":
                return new Sort(Sort.Direction.DESC, "posTags");
        }
        return null;
    }
}
