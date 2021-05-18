package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(value = "PostController") // swagger 리소스 명시
@RequestMapping("/v1/api/posts")
public class PostController {

    private final PostService postService;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회, 최신순") // api tag, 설명
    @GetMapping("/lists/new")
    public ResponseEntity<Object> getMainPosts(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                      @RequestParam(value = "page", defaultValue = "0") Long page,
                                @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        List<PostDto.ListResponse> posts_list = postService.getPostsList(page, pageSize);
        return new ResponseEntity<>(ApiDefaultResponse.res(200,"회고글 최신순 목록 조회 성공.", posts_list), HttpStatus.OK);
    }

    // 조회순(v1 누적)


    @ApiOperation(value = "main", notes = "[메인]회고글 저장하기")
    @PostMapping("/")
    public ResponseEntity<Object> inputPosts(@RequestBody PostDto.saveResponse saveResponse){
        Post post = postService.inputPosts(saveResponse);
        return new ResponseEntity<>(ApiDefaultResponse.res(200,"테스트....", post), HttpStatus.OK);
    }


    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 수정하기")
    @PutMapping("/{postIdx}")
    public ResponseEntity<Object> updatePosts(@ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @PathVariable(value = "postIdx") Long postIdx){

        return new ResponseEntity<>(ApiDefaultResponse.res(200,"",""),HttpStatus.OK);
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 삭제하기")
    @DeleteMapping("/")
    public ResponseEntity<Object> deletePosts(@ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @RequestParam(value = "postIdx") Long postIdx){

        boolean isPost = postService.deletePosts(postIdx);
        if (!isPost){
            return new ResponseEntity<>(ApiDefaultResponse.res(400,"",""),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ApiDefaultResponse.res(200, "",""),HttpStatus.OK);
    }
}
