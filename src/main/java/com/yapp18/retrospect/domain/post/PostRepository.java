package com.yapp18.retrospect.domain.post;

import com.yapp18.retrospect.web.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop1ByOrderByPostIdxDesc();
    List<Post> findTop1ByOrderByViewDesc();
    boolean existsByPostIdxLessThan(Long cursorId);
    boolean existsByViewLessThan(int view);
    List<Post> findAllByUserUserIdx(Long userIdx);
}
