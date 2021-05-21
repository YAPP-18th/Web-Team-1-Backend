package com.yapp18.retrospect.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop1ByOrderByPostIdxDesc();
    List<Post> findTop1ByOrderByViewDesc();
    Optional<Post> findByPostIdx(Long postIdx);
    boolean existsByPostIdxLessThan(Long cursorId);

}
