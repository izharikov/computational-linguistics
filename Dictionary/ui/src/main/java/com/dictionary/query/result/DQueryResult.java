package com.dictionary.query.result;

import com.db.entity.DictWord;

import java.util.HashMap;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class DQueryResult {
    private int pageNo;
    private List<DictWord> words;
    private int allCount;
    private int pageCount;

    public DQueryResult(int pageNo, List<DictWord> words, int allCount, int pageCount) {
        this.pageNo = pageNo;
        this.words = words;
        this.allCount = allCount;
        this.pageCount = pageCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<DictWord> getWords() {
        return words;
    }

    public void setWords(List<DictWord> words) {
        this.words = words;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
