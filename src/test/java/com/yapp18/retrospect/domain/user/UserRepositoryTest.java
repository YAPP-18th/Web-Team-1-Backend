package com.yapp18.retrospect.domain.user;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.web.advice.EntityNullException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RetrospectDataTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private final long USER_IDX = 1L;

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/빈_사용자_테이블.xml"
    })
    @ExpectedDatabase(value = "classpath:dbunit/User/사용자_한명_존재.xml",
            assertionMode= DatabaseAssertionMode.NON_STRICT)
    public void 사용자_정보_등록(){
        User user = EntityCreator.createUserEntity();

        userRepository.save(user);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml"
    })
    public void 사용자_정보_조회(){
        User expected = EntityCreator.createUserEntity();

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        assertThat(user.getUserIdx()).isEqualTo(expected.getUserIdx());
        assertThat(user.getEmail()).isEqualTo(expected.getEmail());
        assertThat(user.getName()).isEqualTo(expected.getName());
        assertThat(user.getNickname()).isEqualTo(expected.getNickname());
        assertThat(user.getProfile()).isEqualTo(expected.getProfile());
        assertThat(user.getJob()).isEqualTo(expected.getJob());
        assertThat(user.getIntro()).isEqualTo(expected.getIntro());
        assertThat(user.getProvider()).isEqualTo(expected.getProvider());
        assertThat(user.getProviderId()).isEqualTo(expected.getProviderId());
        assertThat(user.getRole()).isEqualTo(expected.getRole());
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml"
    })
    @ExpectedDatabase(value = "classpath:dbunit/User/수정된_사용자_정보.xml",
            assertionMode= DatabaseAssertionMode.NON_STRICT)
    public void 사용자_정보_수정(){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        user.updateProfile("수정된이름", "수정된닉네임", "수정된프로필URL", "수정된직업", "수정된자기소개");

        entityManager.flush(); // flush 연산을 통해 데이터베이스에 강제 반영
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml"
    })
    @ExpectedDatabase(value = "classpath:dbunit/User/빈_사용자_테이블.xml",
            assertionMode= DatabaseAssertionMode.NON_STRICT)
    public void 사용자_정보_삭제(){
        User user = userRepository.findById(USER_IDX)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        userRepository.delete(user);

        entityManager.flush(); // flush 연산을 통해 데이터베이스에 강제 반영
    }
}
