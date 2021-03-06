package com.yapp18.retrospect.domain.like;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUserUserIdx(Post post, Long UserIdx);

    @Query(value = "SELECT like_idx FROM like_tb WHERE (like_tb.post_idx =: postIdx) AND (like_tb.user_idx =: userIdx)",  nativeQuery = true)
    Like findByPostIdxAndUserIdx(Long postIdx, Long userIdx);

    List<Like> findByUserOrderByCreatedAtDesc(User user, Pageable page);

    @Query(value = "SELECT * FROM like_tb WHERE like_tb.like_idx < :cursorIdx AND like_tb.user_idx = :userIdx ORDER BY like_tb.like_idx DESC", nativeQuery = true)
    List<Like> cursorFindByUserOrderByCreatedAtDesc(Long cursorIdx, Long userIdx, Pageable page);

    boolean existsByUserAndLikeIdxLessThan(User user, Long lastIdx);

    void deleteByUserAndPost(User user, Post post);
//    List<Like> findByUserOrderByCreatedAtDesc(Long userIdx, Pageable page);
//    Like findByPostAndUser(Post post, User user);
}