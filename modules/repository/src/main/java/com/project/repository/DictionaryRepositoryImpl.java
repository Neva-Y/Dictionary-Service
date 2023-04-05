package com.project.repository;

import com.project.repository.dictionary.DictionaryEntry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Repository
public class DictionaryRepositoryImpl implements DictionaryRepository {
    private static final String COMMA_QUOTES_DELIMITER = "\",\"";
    public final HashMap<String, String[]> dictionary = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DictionaryRepositoryImpl.class);
    @Override
    public synchronized Boolean insertWord(String word, String[] meanings) {
        try {
            if (dictionary.containsKey(StringUtils.capitalize(word))) {
                return Boolean.FALSE;
            } else {
                dictionary.put(StringUtils.capitalize(word), meanings);
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
            if (dictionary.containsKey(StringUtils.capitalize(word))) {
                dictionary.put(StringUtils.capitalize(word), meanings);
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
            if (dictionary.containsKey(StringUtils.capitalize(word))) {
                dictionary.remove(StringUtils.capitalize(word));
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
            String[] meanings = dictionary.getOrDefault(StringUtils.capitalize(word), null);
            if (meanings != null && meanings.length > 0) {
                return new DictionaryEntry(StringUtils.capitalize(word), meanings);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("queryWord failed with exception", e);
            return null;
        }
    }

    @Override
    public Boolean initialiseDictionary(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Boolean flag;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_QUOTES_DELIMITER);
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replace("\"", "");
                }
                // Make sure word has a meaning and the word does not contain spaces
                if (values.length > 1 && !values[0].contains(" ")) {
                    if(insertWord(StringUtils.capitalize(values[0]), Arrays.copyOfRange(values, 1, values.length))) {
                        logger.info("From {}: inserted word {} into the dictionary with meanings {}", fileName,
                                values[0], Arrays.copyOfRange(values, 1, values.length));
                    }
                }
            }
            return Boolean.TRUE;
        } catch (FileNotFoundException e) {
            logger.error("Unable to find file to initialiseDictionary", e);
            return Boolean.FALSE;
        } catch (IOException e) {
            logger.error("initialiseDictionary failed with exception", e);
            return Boolean.FALSE;
        }
    }
}
