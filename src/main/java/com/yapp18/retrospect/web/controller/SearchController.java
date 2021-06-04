package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.SearchService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(value = "SearchController")
@RequestMapping("/api/v1/posts/search")
public class SearchController {

    private final SearchService searchService;
    private static final int DEFAULT_SIZE = 20;

    @ApiOperation(value = "search" ,notes = "[검색] 검색하기 ")
    @GetMapping("")
    public ResponseEntity<Object> findByTitle(@ApiParam(value = "검색할 제목키워드", required = true, example = "st")
            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "type") String type,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
        return new ResponseEntity<>(ApiDefaultResponse.res(200,
                ResponseMessage.SEARCH_TITLE.getResponseMessage(),
                searchService.findPostsByTitle(keyword, type, PageRequest.of(page, pageSize))), HttpStatus.OK);
    }

}
