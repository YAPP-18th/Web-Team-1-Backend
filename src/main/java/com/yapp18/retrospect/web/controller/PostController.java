package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "PostController") // swagger 리소스 명시
@RequestMapping("/v1/api/posts")
public class PostController {

    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회") // api tag, 설명
    @GetMapping("/lists")
    public Long getMainPosts(@ApiParam(value = "페이지 번호", required = true, example = "1")
                                      @RequestParam(value = "page") Long page,
                                      @RequestParam(value = "pageSize") Long pageSize){
        Long test = page;
        return test;
    }
}
