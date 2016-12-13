package com.dictionary.controllers;

import com.db.entity.DictWord;
import com.dictionary.repository.DictionaryRepository;
import com.dictionary.utils.BaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/rest/dictionary/group")
public class RESTGroupController {
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @RequestMapping("/search")
    public List<DictWord> find(@RequestParam(name = "term") String term,
                               @RequestParam(name = "equal", required = false,defaultValue = "false") Boolean equalOnly) {
        if ( !equalOnly) {
            return optimize(dictionaryRepository.findByWordLike("%" + term + "%", null), term);
        } else {
            return Collections.singletonList(dictionaryRepository.findByWord(term));
        }
    }

    @RequestMapping("/words-groups")
    public Collection<DictWord> group(@RequestParam(name = "id") int id) {
        DictWord dictWord = dictionaryRepository.findById(id);
        if (dictWord == null || dictWord.getGroupId() == null) {
            return new ArrayList<>();
        }
        return dictionaryRepository.findByGroupId(dictWord.getGroupId());
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public boolean apply(@RequestParam(name = "ids") String strIds,
                         @RequestParam(name = "init", defaultValue = "-1") Integer initId) {
        List<Integer> ids = BaseUtils.fromJSON(strIds);
        //  here we add all words to one group
        if (ids != null && ids.size() > 0) {
            List<DictWord> words = findByIds(ids);
            Integer gr = words.get(0).getGroupId();
            if (gr == null || gr < 0) {
                Integer max = dictionaryRepository.getMaxGroupId();
                if ( max != null) {
                   gr = max +1;
                } else {
                    gr = 0;
                }
            }
            for (DictWord dw : words) {
                dw.setGroupId(gr);
            }
            dictionaryRepository.save(words);
        }
        //  here we set initial word, if it sent to server
        if ( initId  > 0){
            DictWord d = dictionaryRepository.findById(initId);
            d.setInitial(true);
            dictionaryRepository.save(d);
        }
        return false;
    }

    private List<DictWord> findByIds(List<Integer> ids) {
        List<DictWord> result = new ArrayList<>();
        for (Integer id : ids) {
            result.add(dictionaryRepository.findById(id));
        }
        return result;
    }

    private List<DictWord> optimize(Page<DictWord> source, String term) {
        DictWord dw = dictionaryRepository.findByWord(term);
        Integer grId = -1;
        if (dw != null) {
            grId = dw.getGroupId();
        }
        List<DictWord> dictWords = new LinkedList<>();
        for (DictWord dictWord : source) {
            if (grId == null || !grId.equals(dictWord.getGroupId())) {
                if (Math.abs(dictWord.getWord().length() - term.length()) < 2) {
                    dictWords.add(dictWord);
                }
            }
        }
        if (dictWords.isEmpty()) {
            for (DictWord dictWord : source) {
                if (grId == null || !grId.equals(dictWord.getGroupId())) {
                    if (Math.abs(dictWord.getWord().length() - term.length()) < 3) {
                        dictWords.add(dictWord);
                    }
                }
            }
        }
        return dictWords;
    }
}
