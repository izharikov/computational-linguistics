package com.dictionary.utils;

import com.db.utils.DictionaryUtils;
import com.dict.utils.LobCodesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Ihar Zharykau
 */
@Configuration
@PropertySource(value = { "classpath:lob.properties" })
public class LobCodeUtilsImpl implements LobCodesUtils {
    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void setLobCodesInDictionary() {
        String preSetFileName = prop("preSetWordFile");
        Resource resource = resourceLoader.getResource("classpath:" + preSetFileName);
//        new PreSetWords().invokePreSet(resource);
//        new BaseLOB().invokeBaseSet();
//        DictionaryUtils.saveAll();
    }

    private String prop(String pPropName){
        return environment.getRequiredProperty(pPropName);
    }
}
