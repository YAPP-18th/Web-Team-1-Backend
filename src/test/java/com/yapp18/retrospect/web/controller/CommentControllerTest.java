package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractControllerTest {
    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 댓글_작성() throws Exception {
        mockMvc.perform(
                post("/api/v1/comments")
                        .content("{\"postIdx\": 1, \"comments\": \"댓글9\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_SAVE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.commentIdx").value(9L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments").value("댓글9"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("닉네임1"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_수정() throws Exception {
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
    public void 댓글_리스트_조회() throws Exception {
        int page = 0;
        int size = 2;
        mockMvc.perform(
                get("/api/v1/comments/lists?postIdx=" + POST_IDX + "&page=" + page + "&pageSize=" + size)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].commentIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].comments").value("댓글1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].commentIdx").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].comments").value("댓글2"));
    }

    @Test
    @WithMockRetrospectUser
    public void 댓글_삭제() throws Exception {
        mockMvc.perform(
                delete("/api/v1/comments/" + COMMENT_IDX)
        ).andExpect(status().isNoContent());
    }
}