package com.db.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by igor on 4.10.16.
 */
@Entity
@Table(name = "DICTIONARY_TBL", schema = "dictionary", catalog = "")
public class DictWord {
    private Integer id;
    private String word;
    private int wordCount;
    private Integer groupId;
    private Boolean initial;

    @OneToMany(mappedBy = "word")
    private Set<WordPosAssoc> wordPosAssocs;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Set<WordPosAssoc> getWordPosAssocs() {
        return wordPosAssocs;
    }

    public void setWordPosAssocs(Set<WordPosAssoc> wordPosAssocs) {
        this.wordPosAssocs = wordPosAssocs;
    }

    @Basic
    @Column(name = "word_count")
    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    @Column(name="group_id")
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Column(name="is_initial")
    public Boolean isInitial() {
        return initial;
    }

    public void setInitial(Boolean initial) {
        this.initial = initial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictWord dictWord = (DictWord) o;

        if (wordCount != dictWord.wordCount) return false;
        if (id != null ? !id.equals(dictWord.id) : dictWord.id != null) return false;
        if (word != null ? !word.equals(dictWord.word) : dictWord.word != null) return false;
        if (groupId != null ? !groupId.equals(dictWord.groupId) : dictWord.groupId != null) return false;
        if (initial != null ? !initial.equals(dictWord.initial) : dictWord.initial != null) return false;
        return wordPosAssocs != null ? wordPosAssocs.equals(dictWord.wordPosAssocs) : dictWord.wordPosAssocs == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + wordCount;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (initial != null ? initial.hashCode() : 0);
        result = 31 * result + (wordPosAssocs != null ? wordPosAssocs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DictWord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", wordCount=" + wordCount +
                ", groupId=" + groupId +
                ", initial=" + initial +
                ", wordPosAssocs=" + wordPosAssocs +
                '}';
    }
}
