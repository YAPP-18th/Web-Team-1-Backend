package com.yapp18.retrospect.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //소셜 로그인으로 반환되는 값 중 email을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메소드입니다.
    Optional<User> findByEmail(String email);
    Optional<User> findByUserIdx(Long userIdx);
    boolean existsByNickname(String nickname);

    @Modifying
    @Query(value = "ALTER TABLE user_tb ALTER COLUMN user_idx RESTART WITH 1", nativeQuery = true)
    void restartIdxSequence(); // 테스트 h2 db sequence 초기화용 (postgres에선 사용 불가)
}
