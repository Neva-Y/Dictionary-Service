package com.project.application;

import com.project.repository.DictionaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {
    private DictionaryRepository dictionaryRepository;
    private final Logger logger = LoggerFactory.getLogger(TestJob.class);
    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelay() {
        logger.info("Scheduled job started");
        dictionaryRepository.insertWord("Hi", "FDFD");
        dictionaryRepository.insertWord("Hey", "FSFSDF");
        dictionaryRepository.insertWord("Hey", "Bruh");
    }
}
