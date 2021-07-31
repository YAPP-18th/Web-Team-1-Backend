package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment_tb WHERE comment_tb.post_idx = :postIdx", nativeQuery = true)
    List<Comment> findAllByPostIdx(Long postIdx, Pageable page);

//    @Query(value = "SELECT COUNT(*) FROM comment_tb WHERE comment_tb.post_idx = :postIdx", nativeQuery = true)
    Long countCommentByPost(Post post);

    boolean existsByCommentIdxGreaterThanAndPost(Long cursorIdx, Post post);

    @Modifying
    @Query(value = "ALTER TABLE comment_tb ALTER COLUMN comment_idx RESTART WITH 1", nativeQuery = true)
    void restartIdxSequence(); // 테스트 h2 db sequence 초기화용 (postgres에선 사용 불가)
}