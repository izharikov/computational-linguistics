package com.db.entity;

import javax.persistence.*;

/**
 * @author Ihar Zharykau
 */

@Entity
@Table(name="annotated_files", schema = "dictionary", catalog = "")
public class AnnotatedEntity {
    private Integer id = null;
    private String sourceFileName;

    @Id
    @Column(name="id")
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "file_name")
    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedEntity that = (AnnotatedEntity) o;

        if (id != that.id) return false;
        return sourceFileName != null ? sourceFileName.equals(that.sourceFileName) : that.sourceFileName == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sourceFileName != null ? sourceFileName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnnotatedEntity{" +
                "id=" + id +
                ", sourceFileName='" + sourceFileName + '\'' +
                '}';
    }
}
