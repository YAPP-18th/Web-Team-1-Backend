package com.yapp18.retrospect.api.comment.web;

import com.yapp18.retrospect.api.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.service.CommentService;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractControllerTest {
    private final User newUser = User.builder()
            .email("ybell1028@daum.net")
            .name("real-name")
            .nickname("Walkman")
            .profile("profile-url")
            .provider(AuthProvider.kakao)
            .providerId("12345")
            .role(Role.MEMBER)
            .job("job")
            .intro("intro")
            .build();

    @MockBean
    CommentService commentService; // 주된 관심사, 위의 Mock 객체들을 주입받는다.

    private static final Long COMMENT_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 댓글_작성_테스트() throws Exception {
        // ArgumentCaptor를 사용하면 메서드 호출 여부를 검증하는 과정에서 실제 호출할 때 전달한 인자를 보관할 수 있다.
        // ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        //given
        given(commentService.inputComments(any())).willReturn(EntityCreator.createCommentEntity());

        //when
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
        //given
        given(commentService.updateComments(any())).willReturn(EntityCreator.createCommentEntity());

        //when
        mockMvc.perform(
                patch("/api/v1/comments/1")
                        .content("{\"comments\": \"댓글내용\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_UPDATE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.commentIdx").value(COMMENT_IDX))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments").value("댓글내용"));
    }
}
