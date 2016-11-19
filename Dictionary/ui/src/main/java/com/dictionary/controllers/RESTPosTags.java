package com.dictionary.controllers;

import com.db.entity.PosTag;
import com.db.utils.PosUtilsDB;
import com.dictionary.repository.PosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/rest/pos")
public class RESTPosTags {

    @Autowired
    PosRepository posRepository;

    @RequestMapping("/all")
    public List<PosTag> all(){
        //return PosUtilsDB.all();
        return posRepository.findAll();
    }
}
