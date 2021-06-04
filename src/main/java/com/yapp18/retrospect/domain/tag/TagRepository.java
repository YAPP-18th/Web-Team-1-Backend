package com.yapp18.retrospect.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByPostPostIdx(Long postIdx);

    @Transactional
    @Modifying
    @Query("DELETE FROM Tag t where t.tagIdx in :tagList")
    void deleteAllByTagIdxInQuery(List<Long> tagList);
}
