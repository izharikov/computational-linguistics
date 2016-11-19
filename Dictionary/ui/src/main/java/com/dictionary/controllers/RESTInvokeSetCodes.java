package com.dictionary.controllers;

import com.dict.utils.LobCodesUtils;
import com.dictionary.tags.WordTagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/code")
public class RESTInvokeSetCodes {
    @Autowired
    private WordTagUtils wordTagUtils;

    @RequestMapping("/invoke")
    public void invoke(){
        wordTagUtils.computeAndSetTags();
    }
}
