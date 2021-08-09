package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class SearchService {

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional
    public ApiPagingResultResponse<PostDto.ListResponse> searchByType(SearchDto searchDto, Pageable pageable, Long userIdx){
        // 카테고리 검색 시
        List<PostDto.ListResponse> result;
        if (searchDto.getType().equals("category") && searchDto.getKeyword() == null){ // 검색타입이 카테고리고 키워드가 없음.
           List<Post> posts = getPostCategory(searchDto.getPage(), pageable, searchDto.getQuery()) ;// cursorId, 페이지 정보, 쿼리 넘김(카테고리)
           if (posts.isEmpty()) throw new EntityNullException(ErrorInfo.CONDITION_NULL);

            result = posts.stream().map(post->postMapper.postToListResponse(post, userIdx))
                    .collect(Collectors.toList());
        } else{
            // 통합 검색 시
            List<Post> posts = findAllByType(searchDto.getType(), searchDto.getQuery(), searchDto.getKeyword(), searchDto.getPage(),pageable);
            if (posts.isEmpty()) throw new EntityNullException(ErrorInfo.CONDITION_NULL);

            result = posts.stream().map(post->postMapper.postToListResponse(post, userIdx))
                    .collect(Collectors.toList());
        }

        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
        return new ApiPagingResultResponse<>(isNext(searchDto, pageable, lastIdx), result);
    }


    // 해시태그로 검색
    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsByHashTag(String tag, Long cursorId, Pageable pageable, Long userIdx){
        List<Post> posts = findAllByTag(cursorId, pageable, tag);
        if (posts.isEmpty()) throw new EntityNullException(ErrorInfo.CONDITION_NULL);

        List<PostDto.ListResponse> result= posts.stream()
                .map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
        return new ApiPagingResultResponse<>(isHashTag(lastIdx, pageable, tag), result);
    }


    // 통합 검색 분기처리
    private List<Post> findAllByType(String type, String query, String keyword, Long cursorId, Pageable page){
        return cursorId == null || cursorId == 0 ?
                postQueryRepository.FirstSearch(type, query, keyword, page):
                postQueryRepository.Search(type, query, keyword, page, cursorId);
    }

    // 카테고리 분기처리
    private List<Post> getPostCategory(Long cursorId, Pageable pageable, String query) {
        List<String> queryList = Arrays.asList(query.split(","));
        return cursorId == null || cursorId == 0 ?
                postRepository.findAllByCategoryInOrderByPostIdxDesc(pageable, queryList) : // 가장 최초 포스트
                postRepository.findCategory(cursorId, pageable, queryList);
    }

    // 해시태그 분기처리
    private List<Post> findAllByTag(Long cursorId, Pageable page, String tag){
        System.out.println("=====>>>>>" + tag);
        return cursorId == null || cursorId == 0 ?
                postQueryRepository.findAllByHashTag(tag, page) : // 가장 최초 포스트
                postQueryRepository.findCursorIdByHashTag(cursorId, page, tag);
    }

    // 다음 결과가 있는지 판별
    private boolean isNext(SearchDto searchDto, Pageable pageable, Long cursorId){
        String type = searchDto.getType();

        if (type.equals("category")){
            List<String> queryList = Arrays.asList(searchDto.getQuery().split(","));
            return !postRepository.findCategory(cursorId, pageable, queryList).isEmpty(); // 비어있지 않으니까 false 로 반환되니까 반대로 출력
        }
        return !postQueryRepository.Search(type, searchDto.getQuery(), searchDto.getKeyword(), pageable, cursorId).isEmpty();
    }

    // 해시태그 다음 결과가 있는지 판별
    private boolean isHashTag(Long cursorId, Pageable pageable, String tag){
        return !postQueryRepository.findCursorIdByHashTag(cursorId, pageable, tag).isEmpty();
    }


}
