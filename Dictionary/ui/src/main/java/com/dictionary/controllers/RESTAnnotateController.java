package com.dictionary.controllers;

import com.db.entity.AnnotatedEntity;
import com.dictionary.nlp.AnnotationResponse;
import com.dictionary.nlp.NLPService;
import com.dictionary.nlp.PosUtilsImpl;
import com.dictionary.repository.AnnotateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/rest/annotate/")
public class RESTAnnotateController {
    @Autowired
    AnnotateStore annotateStore;

    @Autowired
    NLPService nlpService;

    @RequestMapping("/last")
    public AnnotationResponse last() {
        return nlpService.lastResponse();
    }

    @PostMapping("/update-word")
    public void updateWordTag(@RequestParam(value = "word") String word,
                              @RequestParam(value = "oldPos") String oldPos,
                              @RequestParam(value = "newPos") String newPos,
                              @RequestParam(value = "index") int index) throws IOException {
        AnnotatedEntity e = nlpService.last();
        AnnotationResponse currentResponse = nlpService.byEntity(e);
        currentResponse.changePosForWord(word, newPos, oldPos, index);
        String fileName = NLPService.getReportPath(e.getId());
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
        currentResponse.setPosTags(null);
        os.writeObject(currentResponse);
        os.close();
    }
}
