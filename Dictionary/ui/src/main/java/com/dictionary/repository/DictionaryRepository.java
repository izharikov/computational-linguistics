package com.dictionary.repository;

import com.db.entity.DictWord;
import com.db.entity.PosTag;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ihar Zharykau
 */

@Transactional
public interface DictionaryRepository extends JpaRepository<DictWord, Integer> {
    DictWord findById(int id);
    DictWord findByWord(String word);
    Page<DictWord> findAll(Pageable pageable);
    Page<DictWord> findByWordLikeAndPosTags_IdIn(String word, List<Integer> posTags, Pageable pageable);
    Page<DictWord> findByWordLike(String word, Pageable pageable);
}
