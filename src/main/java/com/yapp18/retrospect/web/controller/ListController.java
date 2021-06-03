package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.service.ListService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.MypageDto;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> findMyPosts(HttpServletRequest request
//                                              @RequestParam(value = "page", defaultValue = "0") Long page,
//                                              @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize
    ){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        List<MypageDto> myPostsList = listService.findAllPostsByUserIdx(userIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.MY_LIST.getResponseMessage(), myPostsList), HttpStatus.OK);
    }

}
