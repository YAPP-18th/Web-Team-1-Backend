package com.yapp18.retrospect.service;

import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.web.advice.EntityNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

//여기서 Service를 테스트하기 위해서는 Repository 객체를 주입받아야 한다.
//그런데 이것을 주입받으려면 스프링을 띄울 수 밖에 없고, 테스트를 하는데 시간이 오래걸린다.
//무엇보다 데이터가 없는 경우 문제가 발생할 수 있다.
//그래서 Mock를 이용해 테스트를 하면 간단하다

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;

    Comment newComment = Comment.builder()
            .commentIdx(COMMENT_IDX)
            .comments("새로운 댓글내용")
            .user(EntityCreator.createUserEntity())
            .post(EntityCreator.createPostEntity())
            .build();

    @Nested
    @DisplayName("댓글 등록 테스트")
    class Input {
        @Test
        public void 댓글_등록_테스트() {
            //given
            Comment newComment = EntityCreator.createCommentEntity();
            //mocking
            given(postService.findByPostIdx(eq(POST_IDX))).willReturn(newComment.getPost());
            // postService.existsByPostIdx 메서드의 리턴값을 willReturn을 이용해 지정하면 when에서 테스트할 메소드를 호출할때 지정한 값이 사용된다.
            //doNothing().when(methodName).postProcess(any()); // void 메소드에 when을 사용하고 싶을때 사용

            //when
            Comment inputtedComment = commentService.inputComments(newComment);

            //then
            assertThat(newComment.getCommentIdx()).isEqualTo(inputtedComment.getCommentIdx());
            assertThat(newComment.getComments()).isEqualTo(inputtedComment.getComments());
            assertThat(newComment.getUser().getUserIdx()).isEqualTo(inputtedComment.getUser().getUserIdx());
            assertThat(newComment.getPost().getPostIdx()).isEqualTo(inputtedComment.getPost().getPostIdx());
        }

        @Test
        public void 댓글_등록_예외_존재하지_않는_회고글_테스트() {
            //given
            Comment newComment = EntityCreator.createCommentEntity();
            //mocking
            given(postService.findByPostIdx(eq(POST_IDX))).willThrow(new EntityNullException(ErrorInfo.POST_NULL));

            //when
            Throwable throwable = catchThrowable(() -> commentService.inputComments(newComment));
            //then
            assertThat(throwable)
                    .isInstanceOf(EntityNullException.class)
                    .hasMessage(ErrorInfo.POST_NULL.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("댓글 조회 테스트")
    class Get {
        @Test
        public void 댓글_리스트_조회_테스트(){
            //given
            int page = 0;
            int size = 2;
            Comment firstComment = EntityCreator.createCommentEntity();
            Comment secondComment = EntityCreator.createCommentEntity();
            secondComment.setCommentIdx(2L);
            secondComment.setComments("댓글내용2");
            List<Comment> arguList = Arrays.asList(firstComment, secondComment);
            Pageable pageable = PageRequest.of(page, size);
            //mocking
            given(postService.findByPostIdx(eq(POST_IDX))).willReturn(EntityCreator.createPostEntity());
            given(commentRepository.findAllByPost(any(), eq(pageable))).willReturn(arguList);
            //when
            List<Comment> commentList = commentService.getCommmentsListByPostIdx(POST_IDX, pageable);
            //then
            assertThat(commentList.get(0).getCommentIdx()).isEqualTo(firstComment.getCommentIdx());
            assertThat(commentList.get(0).getComments()).isEqualTo(firstComment.getComments());
            assertThat(commentList.get(1).getCommentIdx()).isEqualTo(secondComment.getCommentIdx());
            assertThat(commentList.get(1).getComments()).isEqualTo(secondComment.getComments());
        }

        @Test
        public void 댓글_갯수_조회_테스트(){
            //given
            Comment firstComment = EntityCreator.createCommentEntity();
            Comment secondComment = EntityCreator.createCommentEntity();
            secondComment.setCommentIdx(2L);
            secondComment.setComments("댓글내용2");
            List<Comment> arguList = Arrays.asList(firstComment, secondComment);
            //mocking
            given(postService.findByPostIdx(eq(POST_IDX))).willReturn(EntityCreator.createPostEntity());
            given(commentRepository.countCommentByPost(any())).willReturn((long)arguList.size());
            //when
            Long count = commentService.getCommmentsCountByPostIdx(POST_IDX);
            //then
            assertThat(count).isEqualTo(arguList.size());
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class Update {
        @Test
        public void 댓글_수정_테스트() {
            //given
            Comment oldComment = EntityCreator.createCommentEntity();
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willReturn(Optional.ofNullable(oldComment));
            given(commentRepository.save(any())).willReturn(oldComment);
            //when
            Comment updatedComment = commentService.updateComments(newComment);
            //then
            assertThat(oldComment.getCommentIdx()).isEqualTo(updatedComment.getCommentIdx());
            assertThat(oldComment.getComments()).isEqualTo(updatedComment.getComments());
            //수정일자도 업데이트 하는 방법이 있을까?
        }

        @Test
        public void 댓글_수정_예외_권한_없는_사용자_테스트() {
            //given
            newComment.getUser().setUserIdx(2L);
            Comment oldComment = EntityCreator.createCommentEntity();
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willReturn(Optional.ofNullable(oldComment));
            //when
            Throwable throwable = catchThrowable(() -> commentService.updateComments(newComment));
            //then
            assertThat(throwable)
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TokenErrorInfo.ACCESS_DENIED.getMessage());
        }

        @Test
        public void 댓글_수정_예외_존재하지_않는_댓글_테스트() throws Exception {
            //given
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willThrow(new EntityNullException(ErrorInfo.COMMENT_NULL));
            //when
            Throwable thrown = catchThrowable(() -> commentService.updateComments(newComment));
            //then
            assertThat(thrown)
                    .isInstanceOf(EntityNullException.class)
                    .hasMessage(ErrorInfo.COMMENT_NULL.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    class Delete {
        @Test
        public void 댓글_삭제_테스트() {
            //given
            Comment oldComment = EntityCreator.createCommentEntity();
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willReturn(Optional.ofNullable(oldComment));
            doNothing().when(commentRepository).delete(any());
            //when
            commentService.deleteComments(oldComment.getUser(), oldComment.getCommentIdx());
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willThrow(new EntityNullException(ErrorInfo.COMMENT_NULL));
            //then
            Throwable thrown = catchThrowable(() -> commentService.getCommmentsByIdx(COMMENT_IDX));
            assertThat(thrown)
                    .isInstanceOf(EntityNullException.class)
                    .hasMessage(ErrorInfo.COMMENT_NULL.getErrorMessage());
        }

        @Test
        public void 댓글_삭제_예외_권한_없는_사용자_테스트() {
            //given
            newComment.getUser().setUserIdx(2L);
            Comment oldComment = EntityCreator.createCommentEntity();
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willReturn(Optional.ofNullable(oldComment));
            //when
            Throwable throwable = catchThrowable(() -> commentService.deleteComments(newComment.getUser(), oldComment.getCommentIdx()));
            //then
            assertThat(throwable)
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage(TokenErrorInfo.ACCESS_DENIED.getMessage());
        }

        @Test
        public void 댓글_삭제_예외_존재하지_않는_댓글_테스트() throws Exception {
            //given
            Comment oldComment = EntityCreator.createCommentEntity();
            //mocking
            given(commentRepository.findById(eq(COMMENT_IDX))).willThrow(new EntityNullException(ErrorInfo.COMMENT_NULL));
            //when
            Throwable thrown = catchThrowable(() -> commentService.deleteComments(oldComment.getUser(), oldComment.getCommentIdx()));
            //then
            assertThat(thrown)
                    .isInstanceOf(EntityNullException.class)
                    .hasMessage(ErrorInfo.COMMENT_NULL.getErrorMessage());
        }
    }
}
