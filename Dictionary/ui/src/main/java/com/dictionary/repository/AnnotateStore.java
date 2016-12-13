package com.dictionary.repository;

import com.db.entity.AnnotatedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ihar Zharykau
 */
@Transactional
public interface AnnotateStore extends JpaRepository<AnnotatedEntity, Integer> {
    @Query("select max(annotate.id) from AnnotatedEntity annotate")
    Integer getMaxId();
}
