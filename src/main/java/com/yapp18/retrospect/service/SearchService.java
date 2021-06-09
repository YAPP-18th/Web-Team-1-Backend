package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final PostMapper postMapper;

    // 통합 검색
    @Transactional
    public ApiPagingResultResponse<PostDto.ListResponse> findPostsByKeyword(String title, String type, Long cursorId, Pageable page, Long userIdx){
        List<PostDto.ListResponse> result = findAllByTitle(title, type,cursorId, page)
                .stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
        return new ApiPagingResultResponse<>(postService.isNext(lastIdx), result);
    }


    // 해시태그로 검색
    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsByHashTag(String tag, Long cursorId, Pageable page, Long userIdx){
        List<PostDto.ListResponse> result = findAllByTag(cursorId, page, tag).stream()
                .map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
        return new ApiPagingResultResponse<>(postService.isNext(lastIdx), result);
    }


    // 검색
    private List<Post> findAllByTitle(String title, String type, Long cursorId, Pageable page){
        return cursorId == null || cursorId == 0 ?
                postQueryRepository.findAllByTitleFirst(title, type, page):
                postQueryRepository.findAllByTitle(title, type, cursorId, page, postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
    }

    // 해시태그 검색
    private List<Post> findAllByTag(Long cursorId, Pageable page, String tag){
        System.out.println("=====>>>>>" + tag);
        return cursorId == null || cursorId == 0 ?
                postQueryRepository.findAllByHashTag(tag, page) : // 가장 최초 포스트
                postQueryRepository.findCursorIdByHashTag(cursorId, page, tag,postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
    }

}
