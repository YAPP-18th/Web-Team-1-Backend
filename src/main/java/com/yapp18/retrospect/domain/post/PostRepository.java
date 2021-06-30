package com.yapp18.retrospect.domain.post;

import com.yapp18.retrospect.domain.user.User;
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
    // postIdx의 조회수 찾기
    Post findViewByPostIdx(Long postIdx);

    // 최신 조회
//    List<Post> findAllByOrderByPostIdxDesc(Pageable page); // 첫 조회일 때
//    @Query(value = "SELECT * FROM post_tb  WHERE (post_tb.created_at =:currentAt AND post_tb.post_idx <:cursorId) " +
//            "OR (post_tb.created_at<:currentAt) ORDER BY post_tb.created_at DESC, post_tb.post_idx DESC", nativeQuery = true)
//    List<Post> findRecent(Long cursorId, Pageable page, LocalDateTime currentAt);

    // 조회순 page = 0
    List<Post> findAllByOrderByViewDesc(Pageable page);

    // 커서Id의 post 조회수보다 조회수가 작은 애들을 가져온다. -> 실제 select
    @Query(value = "SELECT * FROM post_tb  WHERE (post_tb.view <:view) " +
            "ORDER BY post_tb.view DESC, post_tb.created_at DESC", nativeQuery = true)
    List<Post> findView(int view, Pageable pageable);

    // 조회순 isNext 값 체크하기 select exists (select true from table_name where table_column=?);
    @Query(value = "SELECT EXISTS(" +
            "SELECT * FROM post_tb  WHERE (post_tb.view <:view) " +
            "ORDER BY post_tb.view DESC, post_tb.created_at DESC)", nativeQuery = true)
    boolean findViewNext(int view);


    // 카테고리 초기 page = 0
    @Query(value = "SELECT * FROM post_tb WHERE post_tb.category IN (:query) ORDER BY post_tb.created_at DESC", nativeQuery = true)
    List<Post> findAllByCategoryInOrderByPostIdxDesc(Pageable page, List<String> query);
    // 카테고리 그 이후
    @Query(value = "SELECT * FROM post_tb  WHERE post_tb.category IN (:query) " +
            "AND post_tb.post_idx <:cursorId " +
            "ORDER BY post_tb.created_at DESC, post_tb.post_idx DESC", nativeQuery = true)
    List<Post> findCategory(Long cursorId,Pageable page, List<String> query);


    List<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM post_tb WHERE post_tb.user_idx = :userIdx AND post_tb.post_idx <:cursorIdx ORDER BY post_tb.created_at DESC")
    List<Post> cursorFindByUserOrderByCreatedAtDesc(Long cursorIdx, Long userIdx, Pageable pageable);
    boolean existsByUserAndPostIdxLessThan(User user, Long lastIdx);
}
