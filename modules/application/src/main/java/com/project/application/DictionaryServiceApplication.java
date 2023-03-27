package com.project.application;

import com.project.repository.DictionaryRepository;
import com.project.repository.DictionaryRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@Configuration
public class DictionaryServiceApplication {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(DictionaryServiceApplication.class);
        new SpringApplicationBuilder(DictionaryServiceApplication.class).run(args);
        logger.info("Application is up!");
    }
}