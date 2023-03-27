package com.project.repository;

import com.project.repository.common.DictionaryError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vavr.control.Either;
import org.springframework.stereotype.Repository;
import java.util.HashMap;

@Repository
public class DictionaryRepositoryImpl implements DictionaryRepository {
    public HashMap<String, String> dictionary = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DictionaryRepositoryImpl.class);
    @Override
    public Either<DictionaryError, Boolean> insertWord(String word, String meaning) {
        try {
            logger.info("Inserting word {}", word);
            if (dictionary.containsKey(word)) {
                return Either.right(Boolean.FALSE);
            } else {
                dictionary.put(word, meaning);
                return Either.right(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, Boolean> updateWord(String word, String meaning) {
        try {
            if (dictionary.containsKey(word)) {
                dictionary.put(word, meaning);
                return Either.right(Boolean.TRUE);
            } else {
                return Either.right(Boolean.FALSE);
            }
        } catch (Exception e) {
            logger.error("insertWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, Boolean> removeWord(String word) {
        try {
            if (dictionary.containsKey(word)) {
                dictionary.remove(word);
                return Either.right(Boolean.TRUE);
            } else {
                return Either.right(Boolean.FALSE);
            }
        } catch (Exception e) {
            logger.error("removeWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, String> queryWord(String word) {
        try {
            return Either.right(dictionary.getOrDefault(word, null));
        } catch (Exception e) {
            logger.error("queryWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }
}
