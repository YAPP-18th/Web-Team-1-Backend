package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.CommentService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.CommentDto;
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
@Api(value = "CommentController") // swagger 리소스 명시
@RequestMapping("/api/v2/comments")
public class CommentController {

    private final CommentService commentService;
    private final TokenService tokenService;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 작성") // api tag, 설명
    @PostMapping("")
    public ResponseEntity<Object> inputComments(HttpServletRequest request,
                                                @RequestBody CommentDto.CommentRequest commentRequest) {
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage(),
                commentService.inputComments(commentRequest, userIdx)), HttpStatus.OK);
    }

//    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 목록 조회") // api tag, 설명
//    @GetMapping("/lists")
//    public ResponseEntity<Object> getCommentsByPostIdx(@ApiParam(value = "post_idx", required = true, example = "20")
//                                                           @RequestParam(value = "postIdx") Long postIdx,
//                                                       @RequestParam(value = "page", defaultValue = "0") Long page,
//                                                       @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize) {
//        if (pageSize == null) pageSize = DEFAULT_SIZE;
//        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage(),
//                commentService.getCommmentsListByPostIdx(page, PageRequest.of(0,pageSize))), HttpStatus.OK);
//    }
}
