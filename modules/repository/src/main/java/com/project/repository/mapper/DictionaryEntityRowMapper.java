package com.project.repository.mapper;

import com.project.repository.dictionary.DictionaryEntity;
import com.project.repository.schema.DictionarySchema;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DictionaryEntityRowMapper
        implements RowMapper<DictionaryEntity> {

    @Override
    public DictionaryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DictionaryEntity(
                rs.getString(DictionarySchema.WORD),
                rs.getString(DictionarySchema.MEANING)
        );
    }
}


