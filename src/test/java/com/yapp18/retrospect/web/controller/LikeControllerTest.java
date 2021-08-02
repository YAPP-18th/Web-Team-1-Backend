package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeControllerTest extends AbstractControllerTest {
    private static final Long POST_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 스크랩_등록() throws Exception {
        mockMvc.perform(
                post("/api/v1/likes")
                        .content("{\"postIdx\": 8}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.LIKE_SAVE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.likeIdx").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postIdx").value(8L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("닉네임4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.profile").value("프로필4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category").value("카테고리8"));
    }

    @Test
    @WithMockRetrospectUser
    public void 스크랩_조회() throws Exception {
        int page = 0;
        int size = 2;
        mockMvc.perform(
                get("/api/v1/likes/lists?page=" + page + "&pageSize=" + size)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.LIKE_FIND.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].likeIdx").value(4L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].postIdx").value(4L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].title").value("제목4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].category").value("카테고리4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].contents").value("내용4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].likeIdx").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].postIdx").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].title").value("제목3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].category").value("카테고리3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].contents").value("내용3"));
    }

    @Test
    @WithMockRetrospectUser
    public void 스크랩_삭제() throws Exception {
        mockMvc.perform(
                delete("/api/v1/likes?postIdx=" + POST_IDX)
        ).andExpect(status().isNoContent());
    }
}