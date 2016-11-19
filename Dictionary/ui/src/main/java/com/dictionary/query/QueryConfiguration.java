package com.dictionary.query;

import com.db.factory.DictFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Ihar Zharykau
 */
@Configuration
public class QueryConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DictionaryQuery dictionaryQuery(){
        Session session = DictFactory.getSession();
        return new DictionaryQueryImpl().setSession(session);
    }
}
