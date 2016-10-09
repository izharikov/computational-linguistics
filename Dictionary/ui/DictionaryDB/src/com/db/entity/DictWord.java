package com.db.entity;

import javax.persistence.*;

/**
 * Created by igor on 4.10.16.
 */
@Entity
@Table(name = "DICTIONARY_TBL", schema = "dictionary", catalog = "")
public class DictWord {
    private int id;
    private String word;
    private int wordCount;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "word_count")
    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictWord dictWord = (DictWord) o;

        if (id != dictWord.id) return false;
        if (wordCount != dictWord.wordCount) return false;
        if (word != null ? !word.equals(dictWord.word) : dictWord.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + wordCount;
        return result;
    }
}
