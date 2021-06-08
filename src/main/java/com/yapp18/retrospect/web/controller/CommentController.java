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
                                                @RequestBody CommentDto.CommentInputRequest commentInputRequest) {
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(201, ResponseMessage.COMMENT_SAVE.getResponseMessage(),
                commentService.inputComments(commentInputRequest, userIdx)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 상세보기") // api tag, 설명
    @GetMapping("/{commentIdx}")
    public ResponseEntity<Object> getCommentsById(@ApiParam(value = "상세보기 comment_idx", required = true, example = "3")
                                                   @PathVariable(value = "commentIdx") Long commentIdx) {
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.COMMENT_DETAIL.getResponseMessage(),
                commentService.getCommmentsByIdx(commentIdx)), HttpStatus.OK);
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 수정") // api tag, 설명
    @PutMapping("/{commentIdx}")
    public ResponseEntity<Object> updateComments(HttpServletRequest request,
                                                 @RequestBody CommentDto.CommentUpdateRequest commentUpdateRequest,
                                                 @ApiParam(value = "수정 comment_idx", required = true, example = "3")
                                                     @PathVariable(value = "commentIdx") Long commentIdx) {
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(201, ResponseMessage.COMMENT_UPDATE.getResponseMessage(),
                commentService.updateCommentsByIdx(commentUpdateRequest, commentIdx, userIdx)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 삭제") // api tag, 설명
    @DeleteMapping("/{commentIdx}")
    public ResponseEntity<Object> deleteComments(HttpServletRequest request,
                                                 @ApiParam(value = "수정 comment_idx", required = true, example = "3")
                                                 @PathVariable(value = "commentIdx") Long commentIdx) {
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        commentService.deleteCommentsByIdx(commentIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.COMMENT_DELETE.getResponseMessage(), commentIdx), HttpStatus.OK);
    }

    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 목록 조회") // api tag, 설명
    @GetMapping("/lists")
    public ResponseEntity<Object> getCommentsByPostIdx(@ApiParam(value = "회고글 post_idx", required = true, example = "20")
                                                           @RequestParam(value = "postIdx") Long postIdx,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize) {
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage(),
                commentService.getCommmentsListByPostIdx(postIdx, PageRequest.of(page, pageSize))), HttpStatus.OK);
    }
}