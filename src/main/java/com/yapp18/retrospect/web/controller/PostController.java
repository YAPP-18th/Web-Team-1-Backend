package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.annotation.CurrentUser;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.ApiIsResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(value = "PostController") // swagger 리소스 명시
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final TokenService tokenService;
    private final UserService userService;
    private final PostMapper postMapper;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회, 누적조회순")
    @GetMapping("/lists")
    public ResponseEntity<Object> getMainPostsOrderByView(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                                              @CurrentUser User user,
                                                          @RequestParam(value = "page", defaultValue = "0") Long page,
                                                          @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200,
                        ResponseMessage.POST_FIND_VIEW.getResponseMessage(),
                        postService.getPostsListView(page, PageRequest.of(0,pageSize), userService.getUserInfoFromToken(user))));
    }

    @ApiOperation(value = "main", notes = "[프로필] 유저 회고글 목록 조회, 생성일자순")
    @GetMapping("/lists/{userIdx}")
    public ResponseEntity<Object> getPostsByUserIdxOrderByCreatedAtDesc(@ApiParam(value = "사용자 user_idx", required = true, example = "3")
                                                                        @PathVariable(value = "userIdx") Long userIdx,
                                                                        @RequestParam(value = "page", defaultValue = "0") Long page,
                                                                        @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200, ResponseMessage.POST_FIND_CREATED.getResponseMessage(),
                        postService.getPostsListCreatedAt(page, userIdx, PageRequest.of(0, pageSize))));
    }


    @ApiOperation(value = "detail", notes = "[상세] 회고글 상세보기")
    @GetMapping("/{postIdx}")
    public ResponseEntity<Object> findPostsContentById(@CurrentUser User user,
                                                       @ApiParam(value = "상세보기 post_idx", required = true, example = "3")
                                                       @PathVariable(value = "postIdx") Long postIdx) {

        Long userIdx = userService.getUserInfoFromToken(user);
        Post post = postService.findPostContents(postIdx, userIdx);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiIsResultResponse<>(postService.isWriter(post.getUser().getUserIdx(), userIdx),
                        postService.isScrap(post, userIdx),
                        postMapper.postToDetailResponse(post))
        );
    }

    @ApiOperation(value = "main", notes = "[메인]회고글 저장하기")
    @PostMapping("")
    public ResponseEntity<Object> inputPosts(@CurrentUser User user,
                                             @RequestBody PostDto.saveResponse saveResponse){
        Long userIdx = userService.getUserInfoFromToken(user);
        Long postIdx = postService.inputPosts(saveResponse, userIdx);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200, ResponseMessage.POST_SAVE.getResponseMessage(), postIdx)
        );
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 수정하기")
    @PutMapping("/{postIdx}")
    public ResponseEntity<Object> updatePosts(@CurrentUser User user,
                                              @ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @PathVariable(value = "postIdx") Long postIdx, @RequestBody PostDto.updateRequest requestDto){
        Long userIdx = userService.getUserInfoFromToken(user);
        Long post = postService.updatePosts(userIdx, postIdx, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200, ResponseMessage.POST_UPDATE.getResponseMessage(), post)
        );
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 삭제하기")
    @DeleteMapping("/{postIdx}")
    public ResponseEntity<Object> deletePosts(@CurrentUser User user,
                                              @ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @PathVariable(value = "postIdx") Long postIdx){
       postService.deletePosts(userService.getUserInfoFromToken(user),postIdx);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200, ResponseMessage.POST_DELETE.getResponseMessage())
        );
    }

}
