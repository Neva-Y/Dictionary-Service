package com.project.repository.dictionary;

public class DictionaryEntry {
    public String word;
    public String[] meanings;

    public DictionaryEntry() {
        super();
    }

    public DictionaryEntry(String word, String[] meanings) {
        this.word = word;
        this.meanings = meanings;
    }
}
