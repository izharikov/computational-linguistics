package com.dictionary.session;

import com.db.factory.DictFactory;
import org.hibernate.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class DictionaryFactory {

    public Session getSession(){
        return DictFactory.getSession();
    }
}
