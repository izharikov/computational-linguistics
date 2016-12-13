package com.db.entity;

import javax.persistence.*;

/**
 * @author Ihar Zharykau
 */
@Entity
@Table(name="pos_word_tbl", schema="dictionary")
public class WordPosAssoc {
    @ManyToOne
    @JoinColumn(name = "word_id")
    private DictWord word;

    @ManyToOne
    @JoinColumn(name = "pos_id")
    private PosTag posTag;

    @Column(name="freq")
    private int frequency;

    public DictWord getWord() {
        return word;
    }

    public void setWord(DictWord word) {
        this.word = word;
    }

    public PosTag getPosTag() {
        return posTag;
    }

    public void setPosTag(PosTag posTag) {
        this.posTag = posTag;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordPosAssoc that = (WordPosAssoc) o;

        if (frequency != that.frequency) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        return posTag != null ? posTag.equals(that.posTag) : that.posTag == null;

    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (posTag != null ? posTag.hashCode() : 0);
        result = 31 * result + frequency;
        return result;
    }
}
