package com.yapp18.retrospect.domain.like;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUserUserIdx(Post post, Long UserIdx);

    @Query(value = "SELECT like_idx FROM like_tb WHERE (like_tb.post_idx =: postIdx) AND (like_tb.user_idx =: userIdx)",  nativeQuery = true)
    Optional<Like> findByPostIdxAndUserIdx(Long postIdx, Long userIdx);

    List<Like> findByUserOrderByCreatedAtDesc(User user, Pageable page);

    @Query(value = "SELECT * FROM like_tb WHERE like_tb.like_idx < :cursorIdx AND like_tb.user_idx = :userIdx ORDER BY like_tb.like_idx DESC", nativeQuery = true)
    List<Like> cursorFindByUserOrderByCreatedAtDesc(Long userIdx, Long cursorIdx, Pageable page);

    boolean existsByUserAndLikeIdxLessThan(User user, Long lastIdx);

    void deleteByUserAndPost(User user, Post post);

    @Modifying
    @Query(value = "ALTER TABLE like_tb ALTER COLUMN like_idx RESTART WITH 1", nativeQuery = true)
    void restartIdxSequence(); // 테스트 h2 db sequence 초기화용 (postgres에선 사용 불가)
}