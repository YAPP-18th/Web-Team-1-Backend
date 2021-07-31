package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.service.CommentService;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractControllerTest {
    @MockBean
    private CommentService commentService; // 주된 관심사, 위의 Mock 객체들을 주입받는다.

    //@Mock은 로직이 삭제된 빈 껍데기라고 보면 된다. 실제로 메서드는 갖고 있지만 내부 구현이 없는 상태이다.
    //@Spy는 모든 기능을 가지고 있는 완전한 객체다.
    //대체로 Spy보다는 Mock을 사용하길 권고한다. 하지만 외부라이브러리를 이용한 테스트에는 @Spy를 사용하는 것을 추천한다.

    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 댓글_작성_테스트() throws Exception {
        // ArgumentCaptor를 사용하면 메서드 호출 여부를 검증하는 과정에서 실제 호출할 때 전달한 인자를 보관할 수 있다.
        // ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // CommentService는 CommentRepository, PostRepository를 주입받아야 한다.
        // 그런데 이렇게 실행하면 테스트는 성공하는 대신에 CommentService 내에 Repository들은 null 상태가 되어있다.
        // 객체주입을 하지 않았기 때문이다.
        given(commentService.inputComments(any())).willReturn(EntityCreator.createCommentEntity());
        // 그럼에도 불구하고 테스트는 통과되는데 해당메서드의 리턴값을 willReturn을 이용해 지정해두었기 때문이다.
        // 실질적으로 Repository의 기능을 사용한 것은 아무것도 없다.

        mockMvc.perform(
                post("/api/v1/comments")
                        .content("{\"postIdx\": 1, \"comments\": \"댓글내용\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_SAVE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.commentIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments").value("댓글내용"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("테스트닉네임"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_수정_테스트() throws Exception {
        given(commentService.updateComments(any())).willReturn(EntityCreator.createCommentEntity());

        mockMvc.perform(
                patch("/api/v1/comments/" + COMMENT_IDX)
                        .content("{\"comments\": \"댓글내용\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_UPDATE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.commentIdx").value(COMMENT_IDX))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments").value("댓글내용"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_리스트_조회_테스트() throws Exception {
        int page = 0;
        int size = 2;
        Comment secondComment = EntityCreator.createCommentEntity();
        secondComment.setCommentIdx(2L);
        secondComment.setComments("댓글내용2");
        secondComment.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 5, 0));
        secondComment.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 5, 0));
        List<Comment> commentList = Arrays.asList(EntityCreator.createCommentEntity(), secondComment);

        given(commentService.getCommmentsListByPostIdx(eq(POST_IDX) , eq(PageRequest.of(page, size)))).willReturn(commentList);

        mockMvc.perform(
                get("/api/v1/comments/lists?postIdx=" + POST_IDX + "&page=" + page + "&pageSize=" + size)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].commentIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].comments").value("댓글내용"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].commentIdx").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].comments").value("댓글내용2"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_삭제_테스트() throws Exception {
        doNothing().when(commentService).deleteComments(any(), eq(COMMENT_IDX));

        mockMvc.perform(
                delete("/api/v1/comments/" + COMMENT_IDX)
        ).andExpect(status().isNoContent());
    }
}