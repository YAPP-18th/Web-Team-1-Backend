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

    @ApiOperation(value = "like", notes = "[스크랩] 회고글 스크랩") // api tag, 설명
    @PostMapping("")
    public ResponseEntity<Object> inputLikes(HttpServletRequest request,
                                             @RequestBody LikeDto.InputRequest inputRequest) {
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(201, ResponseMessage.LIKE_SAVE.getResponseMessage(),
                likeService.inputLikes(inputRequest, userIdx)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "like", notes = "[스크랩] 스크랩 한 글 삭제") // api tag, 설명
    @DeleteMapping("/{likeIdx}")
    public ResponseEntity<Object> deleteLikes(HttpServletRequest request,
                                              @ApiParam(value = "삭제 like_idx", required = true, example = "3")
                                              @PathVariable(value = "likeIdx") Long likeIdx){
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        likeService.deleteLikes(likeIdx);
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
