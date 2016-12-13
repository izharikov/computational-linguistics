package com.dictionary.controllers.annotate;

import com.dictionary.nlp.AnnotationResponse;
import com.dictionary.nlp.NLPService;
import com.dictionary.nlp.PosUtilsImpl;
import com.upload.add.UploadFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Ihar Zharykau
 */
@Controller
public class UploadFileToAnnotateController {

    @Autowired
    private NLPService nlpService;

    @Autowired
    private PosUtilsImpl posUtils;

    @RequestMapping("/upload-and-annotate")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("name") String name) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String inputString = new String(bytes);
                AnnotationResponse response = new AnnotationResponse();
                posUtils.annotateForStatistic(inputString, response);
                nlpService.saveAnnotatedResponse(response, name, inputString);
                return "redirect:";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:";
            }
        } else {
            return "redirect:";
        }
    }

}
