package com.dictionary.utils.annotate;

import com.dictionary.nlp.PosUtilsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class AnnotateUtils {

    @Autowired
    PosUtilsImpl posUtils;

    public void annotate(File fileToAnnotate) {
        try {
            Scanner sc = new Scanner(fileToAnnotate);

        } catch (IOException e) {

        }
    }


}
