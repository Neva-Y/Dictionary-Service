/*
Name: Xing Yang Goh
ID: 1001969
 */
package com.project.repository.dictionary;

import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
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
