package com.yapp18.retrospect.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i where i.imageUrl in :compareList")
    void deleteByImageUrlInQuery(List<String> compareList);


}