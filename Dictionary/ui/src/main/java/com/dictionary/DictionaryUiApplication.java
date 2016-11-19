package com.dictionary;

import com.db.factory.DictFactory;
import com.dictionary.controllers.file.upload.StorageProperties;
import com.dictionary.controllers.file.upload.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.dictionary.repository")
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
