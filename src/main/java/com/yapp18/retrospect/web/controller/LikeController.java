package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.annotation.CurrentUser;
import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.mapper.LikeMapper;
import com.yapp18.retrospect.service.LikeService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
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
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(value = "LikeController") // swagger 리소스 명시
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;
    private final LikeMapper likeMapper;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "like", notes = "[스크랩] 회고글 스크랩")
    @PostMapping("")
    public ResponseEntity<Object> inputLikes(@CurrentUser User user,
                                             @RequestBody LikeDto.InputRequest inputRequest) {
        Like newLike = likeService.inputLikes(user, inputRequest.getPostIdx());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiDefaultResponse.res(201, ResponseMessage.LIKE_SAVE.getResponseMessage(),
                        likeMapper.toDto(newLike)
                )
        );
    }

    @ApiOperation(value = "like", notes = "[스크랩] 스크랩 한 글 목록 조회, 생성일자순")
    @GetMapping("/lists")
    public ResponseEntity<Object> getLikesOrderByCreatedAtDesc(@CurrentUser User user,
                                                               @RequestParam(value = "page", defaultValue = "0") Long page,
                                                               @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;

        List<LikeDto.BasicResponse> result = likeService.getLikeListCreatedAt(user, page, PageRequest.of(0, pageSize));

        Long lastIdx = result.isEmpty() ? null : result.get(result.size() - 1).getLikeIdx();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiDefaultResponse.res(200, ResponseMessage.LIKE_FIND.getResponseMessage(),
                        new ApiPagingResultResponse<>(likeService.isNext(user, lastIdx), result))
                );
    }

    @ApiOperation(value = "like", notes = "[스크랩] 스크랩 한 글 삭제")
    @DeleteMapping("")
    public ResponseEntity<Object> deleteLikes(@CurrentUser User user,
                                              @ApiParam(value = "회고글 idx", required = true, example = "13")
                                              @RequestParam(value = "postIdx") Long postIdx){
        likeService.deleteLikes(user, postIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(204, ResponseMessage.LIKE_SAVE.getResponseMessage()), HttpStatus.NO_CONTENT);
    }
}
