package com.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Ihar Zharykau
 */
@Entity
@Table(name = "pos_tags", schema = "dictionary", catalog = "")
public class PosTag {
    private int id;
    private String name;
    private String description;
    private Set<DictWord> words;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "posTags")
    public Set<DictWord> getWords() {
        return words;
    }

    public void setWords(Set<DictWord> words) {
        this.words = words;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PosTag lobCode = (PosTag) o;

        if (id != lobCode.id) return false;
        return name != null ? name.equals(lobCode.name) : lobCode.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PosTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
