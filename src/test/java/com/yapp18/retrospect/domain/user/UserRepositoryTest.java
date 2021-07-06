package com.yapp18.retrospect.domain.user;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.web.advice.EntityNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RetrospectDataTest
@DatabaseSetup({"classpath:dbunit/User/빈_사용자_테이블.xml"})
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private final long USER_IDX = 1L;

    @BeforeEach
    public void init(){
        userRepository.restartIdxSequence(); // userIdx sequence 초기화
    }

    @Test
    @DatabaseSetup({"classpath:dbunit/User/빈_사용자_테이블.xml"})
    @ExpectedDatabase(value = "classpath:dbunit/User/사용자_한명_존재.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void 사용자_정보_등록() {
        userRepository.save(User.builder()
                .userIdx(USER_IDX)
                .email("test@example.com")
                .name("테스트이름")
                .nickname("테스트닉네임")
                .profile("프로필URL")
                .provider(AuthProvider.kakao)
                .providerId("1")
                .role(Role.MEMBER)
                .job("테스트직업")
                .intro("테스트자기소개")
                .build());

        entityManager.flush();
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    public void 사용자_정보_인덱스로_조회() {
        User expected = EntityCreator.createUserEntity();

        User user = userRepository.findByUserIdx(expected.getUserIdx())
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
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    public void 사용자_정보_이메일로_조회() {
        User expected = EntityCreator.createUserEntity();

        User user = userRepository.findByEmail(expected.getEmail())
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
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    public void 사용자_닉네임_조회_True() {
        User expected = EntityCreator.createUserEntity();

        boolean result = userRepository.existsByNickname(expected.getNickname());

        assertThat(result).isEqualTo(true);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    public void 사용자_닉네임_조회_False() {
        boolean result = userRepository.existsByNickname("존재하지않는닉네임");

        assertThat(result).isEqualTo(false);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    @ExpectedDatabase(value = "classpath:dbunit/User/수정된_사용자_정보.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)

    public void 사용자_정보_수정() {
        User user = userRepository.findById(USER_IDX)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        user.updateProfile("수정된이름", "수정된닉네임", "수정된프로필URL", "수정된직업", "수정된자기소개");

        userRepository.save(user);

        entityManager.flush(); // flush 연산을 통해 데이터베이스에 강제 반영
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/User/사용자_한명_존재.xml")
    @ExpectedDatabase(value = "classpath:dbunit/User/빈_사용자_테이블.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void 사용자_정보_삭제() {
        User user = userRepository.findById(USER_IDX)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        userRepository.delete(user);
    }
}
