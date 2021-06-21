package com.yapp18.retrospect.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface  PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostIdx(Long postIdx);

    boolean existsByPostIdxLessThan(Long cursorId);

    List<Post> findAllByUserUserIdxOrderByCreatedAtDesc(Long userIdx, Pageable pageable);

    // 쓴 날짜 찾기
    Post findCreatedAtByPostIdx(Long postIdx);
    Post findViewByPostIdx(Long postIdx);

    // 최신 조회
//    List<Post> findAllByOrderByPostIdxDesc(Pageable page); // 첫 조회일 때
//    @Query(value = "SELECT * FROM post_tb  WHERE (post_tb.created_at =:currentAt AND post_tb.post_idx <:cursorId) " +
//            "OR (post_tb.created_at<:currentAt) ORDER BY post_tb.created_at DESC, post_tb.post_idx DESC", nativeQuery = true)
//    List<Post> findRecent(Long cursorId, Pageable page, LocalDateTime currentAt);

    // 조회순
    List<Post> findAllByOrderByViewDesc(Pageable page);
    @Query(value = "SELECT * FROM post_tb  WHERE (post_tb.created_at =:currentAt AND post_tb.view <:view) " +
            "OR (post_tb.created_at<:currentAt AND post_tb.view <:view) ORDER BY post_tb.view DESC,post_tb.created_at DESC", nativeQuery = true)
    List<Post> findView(int view, Pageable pageable, LocalDateTime currentAt);

    // 카테고리
    @Query(value = "SELECT * FROM post_tb WHERE post_tb.category IN (:query) ORDER BY post_tb.created_at DESC", nativeQuery = true)
    List<Post> findAllByCategoryInOrderByPostIdxDesc(Pageable page, List<String> query);

    @Query(value = "SELECT * FROM post_tb  WHERE post_tb.category IN (:query) " +
            "AND post_tb.post_idx <:cursorId " +
            "ORDER BY post_tb.created_at DESC, post_tb.post_idx DESC", nativeQuery = true)
    List<Post> findCategory(Long cursorId,Pageable page, List<String> query);

}
