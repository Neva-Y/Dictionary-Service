package com.project.repository;

import com.project.repository.common.DictionaryError;
import com.project.repository.dictionary.DictionaryEntry;
import io.vavr.control.Either;

import java.util.HashMap;
import java.util.List;

public interface DictionaryRepository {

    public Boolean insertWord(String word, String[] meanings);

    public Boolean updateWord(String word, String[] meanings);

    public Boolean removeWord(String word);

    public DictionaryEntry queryWord(String word);

}
