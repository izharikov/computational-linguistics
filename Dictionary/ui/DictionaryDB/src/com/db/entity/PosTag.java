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

    @OneToMany(mappedBy = "posTag")
    private Set<WordPosAssoc> wordPosAssocs;

    public Set<WordPosAssoc> getWordPosAssocs() {
        return wordPosAssocs;
    }

    public void setWordPosAssocs(Set<WordPosAssoc> wordPosAssocs) {
        this.wordPosAssocs = wordPosAssocs;
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

        PosTag posTag = (PosTag) o;

        if (id != posTag.id) return false;
        if (name != null ? !name.equals(posTag.name) : posTag.name != null) return false;
        if (description != null ? !description.equals(posTag.description) : posTag.description != null) return false;
        return wordPosAssocs != null ? wordPosAssocs.equals(posTag.wordPosAssocs) : posTag.wordPosAssocs == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (wordPosAssocs != null ? wordPosAssocs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PosTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", wordPosAssocs=" + wordPosAssocs +
                '}';
    }
}
