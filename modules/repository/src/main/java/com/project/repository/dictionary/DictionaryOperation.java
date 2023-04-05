package com.project.repository.dictionary;

import org.springframework.boot.jackson.JsonComponent;
@JsonComponent
public class DictionaryOperation {
    public String word;
    public String[] meanings;
    public Operation operation;

    public DictionaryOperation() {
        super();
    }
    public DictionaryOperation(String word, String[] meanings, Operation operation) {
        this.word = word;
        this.meanings = meanings;
        this.operation = operation;
    }

    public enum Operation {
        UPDATE,
        REMOVE,
        ADD,
        QUERY
    }
}
