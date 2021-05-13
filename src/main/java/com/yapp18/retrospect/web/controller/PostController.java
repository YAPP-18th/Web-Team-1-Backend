package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.service.PostService;
import com.yapp18.retrospect.web.dto.PostDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@Api(value = "PostController") // swagger 리소스 명시
@RequestMapping("/v1/api/posts")
public class PostController {

    private PostService postService;
    private static final int DEFAULT_SIZE = 20;

    // 회고글 목록 조회
    @ApiOperation(value = "main", notes = "[메인] 회고글 목록 조회") // api tag, 설명
    @GetMapping("/lists")
    public Integer getMainPosts(@ApiParam(value = "현재 페이지 마지막 post_idx", required = true, example = "20")
                                      @RequestParam(value = "page", defaultValue = "0") Long page,
                                   @RequestParam(value = "pageSize") Integer pageSize){
        if (pageSize == null) pageSize = DEFAULT_SIZE;
//        List<Post> posts_list = postService.getPostsList(page, (Pageable) PageRequest.of(0, pageSize));
        return pageSize;
    }

}
