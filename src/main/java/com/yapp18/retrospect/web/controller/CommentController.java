package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.CurrentUser;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.CommentMapper;
import com.yapp18.retrospect.service.CommentService;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.CommentDto;
import com.yapp18.retrospect.web.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(value = "CommentController") // swagger 리소스 명시
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private static final int DEFAULT_SIZE = 10;

    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 작성")
    @PostMapping("")
    // 시큐리티에서 제공하는 @AuthenticationPrincipal을 이용하여 인증된 사용자 정보를 가져오는 @CurrentUser 애노테이션
    public ResponseEntity<Object> inputComments(@CurrentUser User user,
                                                @RequestBody CommentDto.InputRequest inputRequest) {
        //쿼리를 통해 post 엔티티를 가져오는 대신, RequestDto에 들어있는 postIdx로 엔티티를 생성해준다
        //dtoToEntity mapstruct 활용할 것
        Comment newComment = commentMapper.toEntity(inputRequest, user, Post.builder().postIdx(inputRequest.getPostIdx()).build());
        //기존 return new ResponseEntity<> 대신 빌더 패턴을 사용하는게 가독성에 더 좋아보임
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiDefaultResponse.res(201, ResponseMessage.COMMENT_SAVE.getResponseMessage(),
                commentMapper.toDto(commentService.inputComments(newComment)))
        );
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 상세보기")
    @GetMapping("/{commentIdx}")
    public ResponseEntity<Object> getCommentsById(@ApiParam(value = "상세보기 comment_idx", required = true, example = "3")
                                                   @PathVariable(value = "commentIdx") Long commentIdx) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200, ResponseMessage.COMMENT_DETAIL.getResponseMessage(),
                        commentMapper.toDto(commentService.getCommmentsByIdx(commentIdx)))
        );
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 수정")
    @PatchMapping("/{commentIdx}")
    public ResponseEntity<Object> updateComments(@CurrentUser User user,
                                                 @RequestBody CommentDto updateRequest,
                                                 @ApiParam(value = "수정 comment_idx", required = true, example = "3")
                                                     @PathVariable(value = "commentIdx") Long commentIdx) {
        Comment newComment = commentMapper.toEntity(updateRequest, user, commentIdx);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiDefaultResponse.res(201,
                        ResponseMessage.COMMENT_UPDATE.getResponseMessage(),
                        commentMapper.toDto(commentService.updateComments(newComment)))
        );
    }

    @ApiOperation(value = "comment", notes = "[댓글] 댓글 삭제")
    @DeleteMapping("/{commentIdx}")
    public ResponseEntity<Object> deleteComments(@CurrentUser User user,
                                                 @ApiParam(value = "삭제 comment_idx", required = true, example = "3")
                                                 @PathVariable(value = "commentIdx") Long commentIdx) {
        commentService.deleteComments(user, commentIdx);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiDefaultResponse.res(204,
                        ResponseMessage.COMMENT_DELETE.getResponseMessage())
        );
    }

    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 목록 조회")
    @GetMapping("/lists")
    public ResponseEntity<Object> getCommentsByPostIdx(@CurrentUser User user,
                                                       @ApiParam(value = "회고글 post_idx", required = true, example = "20")
                                                       @RequestParam(value = "postIdx") Long postIdx,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize) {
        if (pageSize == null) pageSize = DEFAULT_SIZE;

        List<CommentDto.ListResponse> result = commentService.getCommmentsListByPostIdx(postIdx, PageRequest.of(page, pageSize))
                .stream()
                .map(comment -> commentMapper.toDto(comment, user))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200,
                        ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage(),
                        result)
        );
    }

    @ApiOperation(value = "comment", notes = "[댓글] 회고글에 댓글 갯수 조회")
    @GetMapping("/lists/count")
    public ResponseEntity<Object> getCountByPostIdx(@ApiParam(value = "회고글 post_idx", required = true, example = "20")
                                                        @RequestParam(value = "postIdx") Long postIdx) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiDefaultResponse.res(200,
                        ResponseMessage.COMMENT_FIND_POSTIDX.getResponseMessage(),
                        commentService.getCommmentsCountByPostIdx(postIdx))
        );
    }
}
