package com.yapp18.retrospect.domain.comment;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.domain.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RetrospectDataTest
public class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager entityManager;

    Post post = EntityCreator.createPostEntity();

    @BeforeEach
    public void init(){
        commentRepository.restartIdxSequence(); // userIdx sequence 초기화
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Comment/빈_댓글_테이블.xml"
    })
    @ExpectedDatabase(value = "classpath:dbunit/Comment/댓글_한개_존재.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void 댓글_등록() {
        commentRepository.save(EntityCreator.createCommentEntity());
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Comment/댓글_두개_존재.xml"
    })
    public void 회고글에_달린_댓글_리스트_조회() {
        List<Comment> commentList = commentRepository.findAllByPostIdx(1L, PageRequest.of(0, 2));

        assertThat(commentList.size()).isEqualTo(2);
        assertThat(commentList.get(0).getCommentIdx()).isEqualTo(1);
        assertThat(commentList.get(0).getComments()).isEqualTo("댓글내용1");
        assertThat(commentList.get(1).getCommentIdx()).isEqualTo(2);
        assertThat(commentList.get(1).getComments()).isEqualTo("댓글내용2");
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Comment/댓글_두개_존재.xml"
    })
    public void 회고글에_달린_댓글_커서_조회() {
        Long count = commentRepository.countCommentByPost(post);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Comment/댓글_네개_존재.xml"
    })
//    public void 회고글에_달린_댓글_커서_페이징_True() {
    public void 마지막_댓글_다음_페이지_있는지_True() {
        boolean exist = commentRepository.existsByCommentIdxGreaterThanAndPost(2L, post);

        assertThat(exist).isEqualTo(true);
    }

    @Test
    @DatabaseSetup({
            "classpath:dbunit/User/사용자_한명_존재.xml",
            "classpath:dbunit/Post/회고글_한개_존재.xml",
            "classpath:dbunit/Comment/댓글_네개_존재.xml"
    })
    public void 마지막_댓글_다음_페이지_있는지_False() {
        boolean exist = commentRepository.existsByCommentIdxGreaterThanAndPost(4L, post);

        assertThat(exist).isEqualTo(false);
    }
}