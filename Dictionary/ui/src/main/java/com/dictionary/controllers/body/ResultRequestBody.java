package com.dictionary.controllers.body;

import com.db.entity.PosTag;

import java.util.List;

/**
 * @author Ihar Zharykau
 */
public class ResultRequestBody {
    String term;
    int page;
    String sort;
    List<PosTag> tags;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<PosTag> getTags() {
        return tags;
    }

    public void setTags(List<PosTag> tags) {
        this.tags = tags;
    }
}
