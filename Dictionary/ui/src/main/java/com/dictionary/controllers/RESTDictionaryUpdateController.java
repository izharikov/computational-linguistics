package com.dictionary.controllers;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.dictionary.repository.DictionaryRepository;
import com.dictionary.repository.PosRepository;
import com.dictionary.utils.BaseUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/rest/dictionary/jpa")
public class RESTDictionaryUpdateController {

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    PosRepository posRepository;

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public DictWord update(@RequestParam(value = "word") String word,
                           @RequestParam(value = "id") int id,
                           @RequestParam(value = "count") int count,
                           @RequestParam(value = "pos") String pos) {
        DictWord oldWord = dictionaryRepository.findById(id);
        DictWord dictWord = dictionaryRepository.findByWord(word);
        if (oldWord != null && dictWord != null) {
            if (oldWord.getId() == dictWord.getId()) {
                oldWord.setWord(word);
                oldWord.setPosTags(byJsonIds(pos));
                dictionaryRepository.save(oldWord);
                return oldWord;
            } else {
                dictWord.setWordCount(dictWord.getWordCount() + oldWord.getWordCount());
                dictWord.getPosTags().addAll(byJsonIds(pos));
                dictionaryRepository.delete(oldWord);
                dictionaryRepository.save(dictWord);
                return dictWord;
            }
        } else {
            oldWord.setWord(word);
            oldWord.getPosTags().addAll(byJsonIds(pos));
            dictionaryRepository.save(oldWord);
            return oldWord;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int save(@RequestParam(value = "word") String word,
                    @RequestParam(value = "count") int count,
                    @RequestParam(value = "pos") String pos) {
        DictWord dictToSave = new DictWord();
        dictToSave.setWord(word);
        dictToSave.setWordCount(count);
        dictToSave.setPosTags(byJsonIds(pos));
        return dictionaryRepository.save(dictToSave).getId();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@RequestParam(value = "id")int id){
        dictionaryRepository.delete(id);
    }

    private Set<PosTag> byJsonIds(String jsonIds) {
        List<Integer> list = BaseUtils.fromJSON(jsonIds);
        return posRepository.findByIdIn(list);
    }
}
