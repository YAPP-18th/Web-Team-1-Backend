package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.service.ListService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(value = "MypageListController")
@RequestMapping("/api/v1/lists")
public class ListController {

    private final ListService  listService;
    private final TokenService tokenService;
    private final PostMapper postMapper;

    @ApiOperation(value = "mypage", notes = "[마이페이지] 내가 쓴 글 보기")
    @GetMapping("")
    public ResponseEntity<Object> findMyPosts(HttpServletRequest request,
                                              @RequestParam(value = "page", defaultValue = "0") Long page,
                                              @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize
    ){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        List<PostDto.ListResponse> myPostsList = listService.findAllPostsByUserIdx(userIdx, PageRequest.of(Math.toIntExact(page), pageSize));
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.MY_LIST.getResponseMessage(), myPostsList), HttpStatus.OK);
    }

    @ApiOperation(value = "mypage", notes = "[마이페이지] 최근 읽은 글 조회")
    @GetMapping("/recent")
    public ResponseEntity<Object> findRecentPosts(HttpServletRequest request){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.MY_RECENT_LIST.getResponseMessage(),
                listService.findRecentPosts(userIdx)), HttpStatus.OK);
    }


}
