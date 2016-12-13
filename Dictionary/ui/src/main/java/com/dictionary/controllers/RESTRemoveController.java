package com.dictionary.controllers;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import com.dictionary.repository.DictionaryRepository;
import com.dictionary.repository.PosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.HashSet;
import java.util.List;

/**
 * @author Ihar Zharykau
 */
@RestController
@RequestMapping("/rest")
public class RESTRemoveController {
    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    PosRepository posRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @RequestMapping("/clear-all")
    public void clearAll() {
        List<PosTag> allTags = posRepository.findAll();
        dictionaryRepository.deleteAll();
        posRepository.deleteAll();
        for ( PosTag pos : allTags){
            pos.setWords(new HashSet<>());
        }
        posRepository.save(allTags);
        dictionaryRepository.flush();
        posRepository.flush();
        entityManagerFactory.createEntityManager().clear();
    }
}
