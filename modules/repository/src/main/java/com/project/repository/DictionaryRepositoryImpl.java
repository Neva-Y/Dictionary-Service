package com.project.repository;

import com.project.repository.dictionary.DictionaryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.HashMap;

@Repository
public class DictionaryRepositoryImpl implements DictionaryRepository {
    public final HashMap<String, String[]> dictionary = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DictionaryRepositoryImpl.class);
    @Override
    public synchronized Boolean insertWord(String word, String[] meanings) {
        try {
            logger.info("Inserting word {}", word);
            if (dictionary.containsKey(word)) {
                return Boolean.FALSE;
            } else {
                dictionary.put(word, meanings);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("insertWord failed with exception", e);
            return null;
        }
    }

    @Override
    public synchronized Boolean updateWord(String word, String[] meanings) {
        try {
            if (dictionary.containsKey(word)) {
                dictionary.put(word, meanings);
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("insertWord failed with exception", e);
            return null;
        }
    }

    @Override
    public synchronized Boolean removeWord(String word) {
        try {
            if (dictionary.containsKey(word)) {
                dictionary.remove(word);
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("removeWord failed with exception", e);
            return null;
        }
    }

    @Override
    public DictionaryEntry queryWord(String word) {
        try {
            String[] meanings = dictionary.getOrDefault(word, null);
            if (meanings != null && meanings.length > 0) {
                return new DictionaryEntry(word, meanings);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("queryWord failed with exception", e);
            return null;
        }
    }
}
