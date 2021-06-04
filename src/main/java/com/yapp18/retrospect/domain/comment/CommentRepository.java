package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query(value = "SELECT * FROM commnet_tb  WHERE (comment_tb.comment_idx <:cursorId) " +
//            "ORDER BY comment_tb.created_at, comment_tb.comment_idx", nativeQuery = true)
//    List<Comment> findAllByPostIdx(Long postIdx, Pageable page);
}