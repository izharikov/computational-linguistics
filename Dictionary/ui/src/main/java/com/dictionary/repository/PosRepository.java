package com.dictionary.repository;

import com.db.entity.PosTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Ihar Zharykau
 */

@Transactional
public interface PosRepository extends JpaRepository<PosTag, Integer> {
    List<PosTag> findAll();
    Set<PosTag> findByIdIn(List<Integer> ids);
}
