package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
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
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회, 최신순") // api tag, 설명
    @GetMapping("/lists/new")
    public ResponseEntity<Object> getMainPosts(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                               HttpServletRequest request,
                                               @RequestParam(value = "page", defaultValue = "0") Long page,
                                @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_FIND_RECENT.getResponseMessage(),
                postService.getPostsListRecent(page, PageRequest.of(0, pageSize), userIdx)), HttpStatus.OK);
    }

    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회, 누적조회순")
    @GetMapping("/lists")
    public ResponseEntity<Object> getMainPostsOrderByView(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                                                      HttpServletRequest request,
                                               @RequestParam(value = "page", defaultValue = "0") Long page,
                                               @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_FIND.getResponseMessage(),
                postService.getPostsListView(page, PageRequest.of(0,pageSize), userIdx)), HttpStatus.OK);
    }


    @ApiOperation(value = "detail", notes = "[상세] 회고글 상세보기")
    @GetMapping("/{postIdx}")
    public ResponseEntity<Object> findPostsContentById(HttpServletRequest request,
                                                       @ApiParam(value = "상세보기 post_idx", required = true, example = "3")
                                                       @PathVariable(value = "postIdx") Long postIdx) {
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_DETAIL.getResponseMessage(),
                postService.findPostContents(postIdx, userIdx)), HttpStatus.OK);
    }

//    @ApiOperation(value = "main", notes = "[메인] 카테고리 필터링")
//    @GetMapping("/lists/{category}")
//    public ResponseEntity<Object> findPostsByCategory(@ApiParam(value = "카테고리", required = true, example = "design")
//                                                      @PathVariable(value = "category") String category,
//                                                      @ApiParam(value = "정렬 순서", required = true, example = "recent")
//                                                      @RequestParam(value = "order") String order,
//                                                      @ApiParam(value = "page", required = true, example = "0")
//                                                      @RequestParam(value = "page") Long page,
//                                                      @ApiParam(value = "pageSize", required = true, example = "20")
//                                                      @RequestParam(value = "pageSize") Integer pageSize
//    ){
//        if (pageSize == null) pageSize = DEFAULT_SIZE;
//        ApiPagingResultResponse<PostDto.ListResponse> post_list = postService.getPostsListByContents(category, order, page, pageSize);
//        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_FIND_CATEGORY.getResponseMessage(),post_list), HttpStatus.OK);
//    }


    @ApiOperation(value = "main", notes = "[메인]회고글 저장하기")
    @PostMapping("")
    public ResponseEntity<Object> inputPosts(HttpServletRequest request,
                                             @RequestBody PostDto.saveResponse saveResponse){
        Long postIdx = postService.inputPosts(saveResponse, tokenService.getUserIdx(tokenService.getTokenFromRequest(request)));
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_SAVE.getResponseMessage(), postIdx), HttpStatus.OK);
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 수정하기")
    @PutMapping("/{postIdx}")
    public ResponseEntity<Object> updatePosts(HttpServletRequest request,
                                              @ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @PathVariable(value = "postIdx") Long postIdx, @RequestBody PostDto.updateRequest requestDto){
        Long post = postService.updatePosts(tokenService.getUserIdx(tokenService.getTokenFromRequest(request)), postIdx, requestDto);
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_UPDATE.getResponseMessage(),post),HttpStatus.OK);
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지]회고글 삭제하기")
    @DeleteMapping("/{postIdx}")
    public ResponseEntity<Object> deletePosts(HttpServletRequest request,
                                              @ApiParam(value = "회고글 post_idx", required = true, example = "1")
                                              @PathVariable(value = "postIdx") Long postIdx){
        boolean isPost = postService.deletePosts(tokenService.getUserIdx(tokenService.getTokenFromRequest(request)),postIdx);
        if (!isPost){
            return new ResponseEntity<>(ApiDefaultResponse.res(400,"삭제할 idx 없음...", false),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.POST_DELETE.getResponseMessage(), true),HttpStatus.OK);
    }
}
