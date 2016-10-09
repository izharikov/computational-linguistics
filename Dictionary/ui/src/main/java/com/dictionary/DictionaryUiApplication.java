package com.dictionary;

import com.db.factory.DictFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class DictionaryUiApplication extends SpringBootServletInitializer {

	public static final DictFactory DICT_FACTORY = new DictFactory();

	public static void main(String[] args) {
		SpringApplication.run(DictionaryUiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DictionaryUiApplication.class);
	}
}
