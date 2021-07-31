package com.yapp18.retrospect.service;

import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.mapper.LikeMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @InjectMocks
    private LikeService likeService;

    @Mock
    private PostService postService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeMapper likeMapper;

    private static final Long USER_IDX = 1L;
    private static final Long POST_IDX = 1L;
    private static final Long LIKE_IDX = 1L;

    @Test
    public void 스크랩_등록_테스트() {
        //given
        Like newLike = EntityCreator.createLikeEntity();
        //mocking
        given(postService.findByPostIdx(eq(POST_IDX))).willReturn(newLike.getPost());
        given(likeMapper.toEntity(any(), any())).willReturn(newLike);
        //when
        Like inputtedLike = likeService.inputLikes(newLike.getUser(), POST_IDX);
        //then
        assertThat(newLike.getLikeIdx()).isEqualTo(inputtedLike.getLikeIdx());
        assertThat(newLike.getPost().getPostIdx()).isEqualTo(inputtedLike.getPost().getPostIdx());
        assertThat(newLike.getPost().getTitle()).isEqualTo(inputtedLike.getPost().getTitle());
        assertThat(newLike.getPost().getContents()).isEqualTo(inputtedLike.getPost().getContents());
    }

    @Test
    public void 스크랩을_사용자와_회고글로_조회_테스트() {
        //given
        Like oldLike = EntityCreator.createLikeEntity();
        //mocking
        given(likeRepository.findByPostIdxAndUserIdx(eq(USER_IDX), eq(POST_IDX))).willReturn(Optional.ofNullable(oldLike));
        //when
        Like newLike = likeService.getLikeByPostIdxAndUserIdx(POST_IDX, USER_IDX);
        //then
        assertThat(newLike.getLikeIdx()).isEqualTo(oldLike.getLikeIdx());
        assertThat(newLike.getPost().getPostIdx()).isEqualTo(oldLike.getPost().getPostIdx());
        assertThat(newLike.getUser().getUserIdx()).isEqualTo(oldLike.getUser().getUserIdx());
    }

    @Test
    public void 존재하지_않는_스크랩을_사용자와_회고글로_조회_테스트() {
        //given
        //mocking
        given(likeRepository.findByPostIdxAndUserIdx(eq(POST_IDX), eq(USER_IDX))).willThrow(new EntityNullException(ErrorInfo.LIKE_NULL));
        //when
        Throwable thrown = catchThrowable(() -> likeService.getLikeByPostIdxAndUserIdx(POST_IDX, USER_IDX));
        //then
        assertThat(thrown)
                .isInstanceOf(EntityNullException.class)
                .hasMessage(ErrorInfo.LIKE_NULL.getErrorMessage());
    }

//    @Test
//    public void 댓글_리스트_조회_첫페이지_테스트() {
//        //given
//        int page = 0;
//        int size = 2;
//        User user = EntityCreator.createUserEntity();
//
//        Like firstLike = EntityCreator.createLikeEntity();
//        Like secondLike = EntityCreator.createLikeEntity();
//        Post secondPost = EntityCreator.createPostEntity();
//        secondLike.setLikeIdx(2L);
//        secondPost.setPostIdx(2L);
//        secondLike.setPost(secondPost);
//
//        List<Like> arguList = Arrays.asList(firstLike, secondLike);
//        List<LikeDto.BasicResponse> dtoList = arguList.stream()
//                .map(likeMapper::toDto)
//                .collect(Collectors.toList());
//        PageRequest pageRequest = PageRequest.of(page, size);
//        //mocking
//        given(likeRepository.findByUserOrderByCreatedAtDesc(eq(user), eq(pageRequest))).willReturn(arguList);
//        given(likeMapper.toDto(eq(firstLike))).willReturn(dtoList.get(0));
//        given(likeMapper.toDto(eq(secondLike))).willReturn(dtoList.get(1));
//        //when
//        List<LikeDto.BasicResponse> result = likeService.getLikeListCreatedAt(user, 0L, pageRequest);
//        //then
//        assertThat(result.get(0).getLikeIdx()).isEqualTo(firstLike.getLikeIdx());
//        assertThat(result.get(0).getPostIdx()).isEqualTo(firstLike.getPost().getPostIdx());
//        assertThat(result.get(1).getLikeIdx()).isEqualTo(secondLike.getLikeIdx());
//        assertThat(result.get(1).getPostIdx()).isEqualTo(secondLike.getPost().getPostIdx());
//    }

    @Test
    public void 스크랩_삭제_테스트() {
        //given
        Like like = EntityCreator.createLikeEntity();
        //mocking
        given(likeRepository.findByPostIdxAndUserIdx(eq(POST_IDX), eq(USER_IDX))).willReturn(Optional.ofNullable(like));
        doNothing().when(likeRepository).delete(any());
        //when
        likeService.deleteLikes(like.getUser(), POST_IDX);
        //mocking
        given(likeRepository.findByPostIdxAndUserIdx(eq(POST_IDX), eq(USER_IDX))).willThrow(new EntityNullException(ErrorInfo.LIKE_NULL));
        //then
        Throwable thrown = catchThrowable(() -> likeService.getLikeByPostIdxAndUserIdx(POST_IDX, USER_IDX));
        assertThat(thrown)
                .isInstanceOf(EntityNullException.class)
                .hasMessage(ErrorInfo.LIKE_NULL.getErrorMessage());
    }

//    @Test
//    public void 스크랩_삭제_예외_권한_없는_사용자_테스트() {
//        //given
//        Like newLike = EntityCreator.createLikeEntity();
//        newLike.getUser().setUserIdx(2L);
//        Like oldLike = EntityCreator.createLikeEntity();
//        //mocking
//        given(likeRepository.findByPostIdxAndUserIdx(eq(POST_IDX), eq(USER_IDX))).willReturn(Optional.ofNullable(oldLike));
//        //when
//        Throwable throwable = catchThrowable(() -> likeService.deleteLikes(newLike.getUser(), POST_IDX);
//        //then
//        assertThat(throwable)
//                .isInstanceOf(AccessDeniedException.class)
//                .hasMessage(TokenErrorInfo.ACCESS_DENIED.getMessage());
//    }

    @Test
    public void 댓글_삭제_예외_존재하지_않는_댓글_테스트() throws Exception {
        //given
        //mocking
        given(likeRepository.findByPostIdxAndUserIdx(eq(POST_IDX), eq(USER_IDX))).willThrow(new EntityNullException(ErrorInfo.LIKE_NULL));
        //when
        Throwable thrown = catchThrowable(() -> likeService.getLikeByPostIdxAndUserIdx(POST_IDX, USER_IDX));
        //then
        assertThat(thrown)
                .isInstanceOf(EntityNullException.class)
                .hasMessage(ErrorInfo.LIKE_NULL.getErrorMessage());
    }
}
