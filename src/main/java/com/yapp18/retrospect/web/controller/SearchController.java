package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.service.SearchService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.SearchDto;
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
@Api(value = "SearchController")
@RequestMapping("/api/v1/posts/search")
public class SearchController {

    private final SearchService searchService;
    private final TokenService tokenService;
    private static final int DEFAULT_SIZE = 20;


    @ApiOperation(value = "search", notes = "[검색] 카테고리 및 통합검색하기")
    @GetMapping("")
    public ResponseEntity<Object> searchByType(HttpServletRequest request, SearchDto searchDto){
        if (searchDto.getPageSize() == null) searchDto.setPageSize(DEFAULT_SIZE);
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(200, "검색 테스트",
                searchService.searchByType(searchDto, PageRequest.of(0, searchDto.getPageSize()), userIdx)
        ), HttpStatus.OK);
    }


    @ApiOperation(value = "search" ,notes = "[검색] 해시태그  검색하기 ")
    @GetMapping("/tag")
    public ResponseEntity<Object> findPostsByHashTag(HttpServletRequest request,
                                                      @ApiParam(value = "태그", required = true, example = "난나")
                                                      @RequestParam(value = "tag") String tag,
                                                      @ApiParam(value = "page", required = true, example = "0")
                                                      @RequestParam(value = "page") Long page,
                                                      @ApiParam(value = "pageSize", required = true, example = "20")
                                                      @RequestParam(value = "pageSize") Integer pageSize
    ){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_FIND_CATEGORY.getResponseMessage(),
                searchService.getPostsByHashTag(tag, page, PageRequest.of(0,pageSize), userIdx)), HttpStatus.OK);
    }



    //    @ApiOperation(value = "search" ,notes = "[검색] 검색하기 ")
//    @GetMapping("")
//    public ResponseEntity<Object> findByTitle(@ApiParam(value = "검색할 제목키워드", required = true, example = "st")
//                                                      HttpServletRequest request,
//            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "type") String type,
//                                              @RequestParam(value = "page", defaultValue = "0") Long page,
//                                              @RequestParam(value = "pageSize") Integer pageSize){
//        if (pageSize == null) pageSize = DEFAULT_SIZE;
//        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
//        return new ResponseEntity<>(ApiDefaultResponse.res(200,
//                ResponseMessage.SEARCH_TITLE.getResponseMessage(),
//                searchService.findPostsByKeyword(keyword, type,page, PageRequest.of(0, pageSize), userIdx)), HttpStatus.OK);
//    }

//    @ApiOperation(value = "search" ,notes = "[검색] 카테고리(해시태그) 검색하기 ")
//    @GetMapping("/category")
//    public ResponseEntity<Object> findPostsByCategory(HttpServletRequest request,
//                                                      @ApiParam(value = "카테고리", required = true, example = "design")
//                                                      @RequestParam(value = "query") String query,
//                                                      @ApiParam(value = "page", required = true, example = "0")
//                                                      @RequestParam(value = "page") Long page,
//                                                      @ApiParam(value = "pageSize", required = true, example = "20")
//                                                      @RequestParam(value = "pageSize") Integer pageSize
//    ){
//        if (pageSize == null) pageSize = DEFAULT_SIZE;
//        Long userIdx = (tokenService.getTokenFromRequest(request) != null) ? tokenService.getUserIdx(tokenService.getTokenFromRequest(request)) : 0L;
//        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.POST_FIND_CATEGORY.getResponseMessage(),
//                searchService.getPostsByCategory(query, page, PageRequest.of(0,pageSize), userIdx)), HttpStatus.OK);
//    }



}
