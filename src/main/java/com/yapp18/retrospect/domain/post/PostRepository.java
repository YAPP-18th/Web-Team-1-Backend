package com.yapp18.retrospect.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop1ByOrderByPostIdxDesc();
    List<Post> findTop1ByOrderByViewDesc();
    boolean existsByPostIdxLessThan(Long cursorId);

}
