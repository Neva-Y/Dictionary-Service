package com.project.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    @Autowired
    DictionaryRepository dictionaryRepository;
    private final Logger logger = LoggerFactory.getLogger(TestJob.class);
    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelay() {
        logger.info("Scheduled job started");
        dictionaryRepository.insertWord("Hi", "FDFD");
        dictionaryRepository.insertWord("Hey", "FSFSDF");
        dictionaryRepository.insertWord("Hey", "Bruh");
    }
}
