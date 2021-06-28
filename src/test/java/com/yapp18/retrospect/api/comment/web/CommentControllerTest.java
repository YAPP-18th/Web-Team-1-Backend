package com.yapp18.retrospect.api.comment.web;

import com.yapp18.retrospect.api.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractControllerTest {
    @MockBean
    CommentService commentService; // 주된 관심사, 위의 Mock 객체들을 주입받는다.

    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 댓글_작성_테스트() throws Exception {
        // ArgumentCaptor를 사용하면 메서드 호출 여부를 검증하는 과정에서 실제 호출할 때 전달한 인자를 보관할 수 있다.
        // ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        given(commentService.inputComments(any())).willReturn(EntityCreator.createCommentEntity());

        mockMvc.perform(
                post("/api/v1/comments")
                        .content("{\"postIdx\": 1, \"comments\": \"정말 좋은 글입니다.\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_SAVE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.commentIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("테스트닉네임"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_수정_테스트() throws Exception {
        given(commentService.updateComments(any())).willReturn(EntityCreator.createCommentEntity());

        mockMvc.perform(
                patch("/api/v1/comments/1")
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
        secondComment.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 5, 0));
        secondComment.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 5, 0));
        List<Comment> commentList = Arrays.asList(EntityCreator.createCommentEntity(), secondComment);

        given(commentService.getCommmentsListByPostIdx(eq(POST_IDX) , eq(PageRequest.of(page, size)))).willReturn(commentList);

        mockMvc.perform(
                get("/api/v1/comments/lists?postIdx=" + POST_IDX + "&page=" + page + "&pageSize=" + size)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].commentIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].commentIdx").value(2L));
    }
}
