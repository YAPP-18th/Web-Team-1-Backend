package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.mapper.LikeMapper;
import com.yapp18.retrospect.service.LikeService;
import com.yapp18.retrospect.web.AbstractControllerTest;
import com.yapp18.retrospect.web.dto.LikeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeControllerTest extends AbstractControllerTest {
    @MockBean
    private LikeService likeService;

    @Autowired
    private LikeMapper likeMapper;

    //@Mock은 로직이 삭제된 빈 껍데기라고 보면 된다. 실제로 메서드는 갖고 있지만 내부 구현이 없는 상태이다.
    //@Spy는 모든 기능을 가지고 있는 완전한 객체다.
    //대체로 Spy보다는 Mock을 사용하길 권고한다. 하지만 외부라이브러리를 이용한 테스트에는 @Spy를 사용하는 것을 추천한다.

    private static final Long POST_IDX = 1L;

    @Test
    @WithMockRetrospectUser
    public void 스크랩_등록() throws Exception {
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

    @Test
    @WithMockRetrospectUser
    public void 스크랩_조회() throws Exception {
        int page = 0;
        int size = 2;
        Like secondLike = EntityCreator.createLikeEntity();
        Post secondPost = EntityCreator.createPostEntity();
        secondPost.setPostIdx(2L);
        secondPost.setTitle("회고글제목2");
        secondPost.setCategory("카테고리2");
        secondPost.setContents("글내용2");
        secondLike.setLikeIdx(2L);
        secondLike.setPost(secondPost);

        List<Like> likeList = Arrays.asList(EntityCreator.createLikeEntity(), secondLike);

        List<LikeDto.BasicResponse> result = likeList.stream()
                .map(like -> likeMapper.toDto(like))
                .collect(Collectors.toList());

        given(likeService.getLikeListCreatedAt(any(), eq(0L), eq(PageRequest.of(0, size)))).willReturn(result);

        mockMvc.perform(
                get("/api/v1/likes/lists?page=" + page + "&pageSize=" + size)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value(ResponseMessage.LIKE_FIND.getResponseMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].likeIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].postIdx").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].title").value("회고글제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].category").value("카테고리"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[0].contents").value("글내용"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].likeIdx").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].postIdx").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].title").value("회고글제목2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].category").value("카테고리2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.result[1].contents").value("글내용2"));
    }

    @Test
    @WithMockRetrospectUser
    public void 스크랩_삭제() throws Exception {
        doNothing().when(likeService).deleteLikes(any(), eq(POST_IDX));

        mockMvc.perform(
                delete("/api/v1/likes?postIdx=" + POST_IDX)
        ).andExpect(status().isNoContent());
    }
}