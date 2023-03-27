package com.project.repository;

import com.project.repository.common.DictionaryError;
import com.project.repository.dictionary.DictionaryEntity;
import com.project.repository.mapper.DictionaryEntityRowMapper;
import com.project.repository.schema.DictionarySchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import io.vavr.control.Either;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DictionaryRepositoryImpl implements DictionaryRepository {
    private NamedParameterJdbcOperations jdbcOperations;
    private final Logger logger = LoggerFactory.getLogger(DictionaryRepositoryImpl.class);
    @Override
    public Either<DictionaryError, Boolean> insertWord(String word, String meaning) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue(DictionarySchema.WORD, word)
                    .addValue(DictionarySchema.MEANING, meaning);
            String query = "INSERT INTO" + DictionarySchema.TABLE_NAME + "VALUES (:word :meaning) ON CONFLICT"
                    + DictionarySchema.WORD + "DO NOTHING";
            return (Either.right(jdbcOperations.update(query, namedParameters) == 1));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, Boolean> updateWord(String word, String meaning) {
        try {
            if (queryWord(word).get().size() == 0) {
                return Either.right(Boolean.FALSE);
            } else if (queryWord(word).get().size() == 1) {
                SqlParameterSource namedParameters = new MapSqlParameterSource()
                        .addValue(DictionarySchema.WORD, word)
                        .addValue(DictionarySchema.MEANING, meaning);
                String query = "INSERT INTO" + DictionarySchema.TABLE_NAME + "VALUES (:word,:meaning) ON CONFLICT"
                        + DictionarySchema.WORD + "DO UPDATE SET" + DictionarySchema.MEANING + "(:meaning)";
                return Either.right(jdbcOperations.update(query, namedParameters) == 1);
            }
            logger.error("Unexpected queryWord response: {}", queryWord(word));
            return Either.left(DictionaryError.INTERNAL_ERROR);
        } catch (Exception e) {
            logger.error("insertWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, Boolean> removeWord(String word) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue(DictionarySchema.WORD, word);
            String query = "DELETE FROM" + DictionarySchema.TABLE_NAME + "WHERE (:word) in" + DictionarySchema.WORD;
            return Either.right(jdbcOperations.update(query, namedParameters) == 1);
        } catch (Exception e) {
            logger.error("removeWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }

    @Override
    public Either<DictionaryError, List<DictionaryEntity>> queryWord(String word) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue(DictionarySchema.WORD, word);
            String query = "SELECT * FROM" + DictionarySchema.TABLE_NAME + "WHERE (:word) in" + DictionarySchema.WORD;
            return Either.right(jdbcOperations.query(query, namedParameters, new DictionaryEntityRowMapper()));
        } catch (Exception e) {
            logger.error("queryWord failed with exception", e);
            return Either.left(DictionaryError.INTERNAL_ERROR);
        }
    }
}
