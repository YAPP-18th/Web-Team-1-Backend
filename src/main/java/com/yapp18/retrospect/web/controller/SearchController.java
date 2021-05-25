package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.SearchService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.management.resource.internal.ResourceNatives;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(value = "SearchController")
@RequestMapping("/api/v1/posts/search")
public class SearchController {

    private final SearchService searchService;

    @ApiOperation(value = "search" ,notes = "[검색] 제목으로 검색")
    @GetMapping("/")
    public ResponseEntity<Object> findByTitle(@ApiParam(value = "검색할 제목키워드", required = true, example = "st")
            @RequestParam(value = "title") String title){

        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.SEARCH_TITLE.getResponseMessage(),""), HttpStatus.OK);
    }

    @ApiOperation(value = "search" ,notes = "[검색] 제목으로 검색")
    @GetMapping("/")
    public ResponseEntity<Object> findByContent(@ApiParam(value = "검색할 내용 ", required = true, example = "내용 ")
                                              @RequestParam(value = "content") String content){

        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.SEARCH_CONTENTS.getResponseMessage(),""), HttpStatus.OK);
    }

    @ApiOperation(value = "search" ,notes = "[검색] 제목으로 검색")
    @GetMapping("/")
    public ResponseEntity<Object> findByAll(@ApiParam(value = "검색할 제목키워드", required = true, example = "st")
                                              @RequestParam(value = "all") String all){

        return new ResponseEntity<>(ApiDefaultResponse.res(200,ResponseMessage.SEARCH_ALL.getResponseMessage(),""), HttpStatus.OK);
    }
}
