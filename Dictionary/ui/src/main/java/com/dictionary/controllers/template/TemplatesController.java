package com.dictionary.controllers.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ihar Zharykau
 */
@Controller
public class TemplatesController {
    @GetMapping("/")
    public String index(){
        return "main";
    }

    @GetMapping("/annotation")
    public String annotation(){
        return "annotate-info";
    }

    @GetMapping("/word-group")
    public String wordGroup(){
        return "group";
    }
}
