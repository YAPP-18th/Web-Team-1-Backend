package com.yapp18.retrospect.domain.like;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RetrospectDataTest
public class LikeRepositoryTest {
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    EntityManager entityManager;

    private final long USER_IDX = 1;
    private final long LIKE_IDX = 1;

    @BeforeEach
    public void init(){
        likeRepository.restartIdxSequence(); // likeIdx sequence 초기화
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
    })
    @ExpectedDatabase(value = "classpath:dbunit/Like/스크랩_한개_존재.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void 스크랩_등록() {
        likeRepository.save(EntityCreator.createLikeEntity());
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_두명_존재.xml",
            "classpath:dbunit/Post/회고글_두개_존재.xml",
            "classpath:dbunit/Like/스크랩_두개_존재.xml"
    })
    public void 스크랩_조회_유저_별_생성일자_내림차순() {
        User user = EntityCreator.createUserEntity();

        List<Like> listList = likeRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 2));

        assertThat(listList.size()).isEqualTo(2);
        //내림차순이니까 2부터
        assertThat(listList.get(0).getLikeIdx()).isEqualTo(2L);
        assertThat(listList.get(0).getPost().getPostIdx()).isEqualTo(2L);
        assertThat(listList.get(1).getLikeIdx()).isEqualTo(1L);
        assertThat(listList.get(1).getPost().getPostIdx()).isEqualTo(1L);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_두명_존재.xml",
            "classpath:dbunit/Post/회고글_네개_존재.xml",
            "classpath:dbunit/Like/스크랩_다섯개_존재.xml"
    })
    public void 스크랩_커서_조회_유저_별_생성일자_내림차순() {
        List<Like> listList = likeRepository.cursorFindByUserOrderByCreatedAtDesc(USER_IDX, 5L, PageRequest.of(0, 2));

        assertThat(listList.size()).isEqualTo(2);
        //내림차순이니까 2부터
        assertThat(listList.get(0).getLikeIdx()).isEqualTo(2L);
        assertThat(listList.get(0).getPost().getPostIdx()).isEqualTo(2L);
        assertThat(listList.get(1).getLikeIdx()).isEqualTo(1L);
        assertThat(listList.get(1).getPost().getPostIdx()).isEqualTo(1L);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_두명_존재.xml",
            "classpath:dbunit/Post/회고글_네개_존재.xml",
            "classpath:dbunit/Like/스크랩_다섯개_존재.xml"
    })
    public void 스크랩_페이징_True() {
        User user = EntityCreator.createUserEntity();

        boolean result = likeRepository.existsByUserAndLikeIdxLessThan(user, 4L);

        assertThat(result).isEqualTo(true);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_두명_존재.xml",
            "classpath:dbunit/Post/회고글_네개_존재.xml",
            "classpath:dbunit/Like/스크랩_다섯개_존재.xml"
    })
    public void 스크랩_페이징_False() {
        User user = EntityCreator.createUserEntity();
        user.setUserIdx(2L);

        boolean result = likeRepository.existsByUserAndLikeIdxLessThan(user, 3L);

        assertThat(result).isEqualTo(false);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Like/스크랩_한개_존재.xml"
    })
    @ExpectedDatabase(value = "classpath:dbunit/common/빈_테이블.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void 스크랩_삭제_사용자_회고글() {
        User user = EntityCreator.createUserEntity();
        Post post = EntityCreator.createPostEntity();

        likeRepository.deleteByUserAndPost(user, post);
    }
}
