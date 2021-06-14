package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.LikeService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.LikeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(value = "LikeController") // swagger 리소스 명시
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;
    private final TokenService tokenService;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "like", notes = "[스크랩] 회고글 스크랩")
    @PostMapping("")
    public ResponseEntity<Object> inputLikes(HttpServletRequest request,
                                             @RequestBody LikeDto.InputRequest inputRequest) {
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(201, ResponseMessage.LIKE_SAVE.getResponseMessage(),
                likeService.inputLikes(inputRequest, userIdx)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "like", notes = "[스크랩] 스크랩 한 글 목록 조회, 누적순")
    @GetMapping("/lists")
    public ResponseEntity<Object> getLikesOrderByCreatedAtDesc(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                                                  HttpServletRequest request,
                                                          @RequestParam(value = "page", defaultValue = "0") Long page,
                                                          @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return ResponseEntity.status(HttpStatus.OK)
        .body(ApiDefaultResponse.res(200, ResponseMessage.LIKE_FIND.getResponseMessage(),
                likeService.getLikeListCreatedAt(PageRequest.of(page.intValue(), pageSize), userIdx)));
    }

    @ApiOperation(value = "like", notes = "[스크랩] 스크랩 한 글 삭제")
    @DeleteMapping("")
    public ResponseEntity<Object> deleteLikes(HttpServletRequest request,
                                              @ApiParam(value = "회고글 idx", required = true, example = "13")
                                              @RequestParam(value = "postIdx") Long postIdx){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        likeService.deleteLikes(userIdx, postIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(204, ResponseMessage.LIKE_SAVE.getResponseMessage()), HttpStatus.NO_CONTENT);
    }

//    @ApiOperation(value = "like", notes = "[스크랩] 회고글 스크랩") // api tag, 설명
//    @PostMapping("")
//    public ResponseEntity<Object> inputLikes(HttpServletRequest request,
//                                               @RequestBody LikeDto.InputRequest inputRequest){
//        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
//        if(likeService.isExist(inputRequest, userIdx)){ // 중복 검사
//            return new ResponseEntity<>(ApiDefaultResponse.res(201, ResponseMessage.LIKE_SAVE.getResponseMessage(),
//                    likeService.inputLikes(inputRequest, userIdx)), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(ApiDefaultResponse.res(500, ResponseMessage.LIKE_SAVE.getResponseMessage(),
//                    likeService.inputLikes(inputRequest, userIdx)), HttpStatus.);
//        }
//    }
}
