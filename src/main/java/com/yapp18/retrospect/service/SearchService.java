package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SearchService {

    private PostQueryRepository postQueryRepository;
    private PostService postService;

    // 제목으로 검색
//    public PostDto.ListResponse findPostsByTitle(String title){
//
//        return ;
//    }

}
