package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.LikeService;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeControllerTest extends AbstractControllerTest {
    @MockBean
    private LikeService likeService;

    //@Mock은 로직이 삭제된 빈 껍데기라고 보면 된다. 실제로 메서드는 갖고 있지만 내부 구현이 없는 상태이다.
    //@Spy는 모든 기능을 가지고 있는 완전한 객체다.
    //대체로 Spy보다는 Mock을 사용하길 권고한다. 하지만 외부라이브러리를 이용한 테스트에는 @Spy를 사용하는 것을 추천한다.

    private static final Long POST_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 스크랩_등록_테스트() throws Exception {
        given(likeService.inputLikes(any(), eq(POST_IDX))).willReturn(EntityCreator.createLikeEntity());

        mockMvc.perform(
                post("/api/v1/likes")
                        .content("{\"postIdx\": 1}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.LIKE_SAVE.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.likeIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("테스트닉네임"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.profile").value("프로필URL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category").value("카테고리"));
    }
}