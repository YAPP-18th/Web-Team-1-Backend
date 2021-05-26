package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.ListService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(value = "MypageListController")
@RequestMapping("/api/v1/lists")
public class ListController {

    private final ListService listService;
    private final TokenService tokenService;

    @ApiOperation(value = "mypage", notes = "[마이페이지] 내가 쓴 글 보기")
    @GetMapping("")
    public ResponseEntity<Object> findMyPosts(@RequestHeader(value="Authorization") String token){
        List<PostDto.ListResponse> myPostsList = listService.findMyPostsByUserIdx(tokenService.getUserIdx(token));
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.MY_LIST.getResponseMessage(),myPostsList), HttpStatus.OK);
    }


}
