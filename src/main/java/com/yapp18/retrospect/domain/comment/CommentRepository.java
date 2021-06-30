package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post, Pageable page);

    boolean existsByCommentIdxGreaterThanAndPost(Long cursorIdx, Post post);

    Long countCommentByPost(Post postIdx);
}