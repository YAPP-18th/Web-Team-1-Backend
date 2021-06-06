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

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final PostMapper postMapper;

    // 제목으로 검색: 최신순 반환.
    @Transactional
    public ApiPagingResultResponse<PostDto.ListResponse> findPostsByTitle(String title, String type, Pageable page, Long userIdx){
        List<PostDto.ListResponse> result = postQueryRepository.findAllByTitleFirst(title, type, page)
                .stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
        return new ApiPagingResultResponse<>(postService.isNext(lastIdx), result);
    }

//    private List<Post> findAllByTitle(String title, String type, Long cursorId, Pageable page){
//        return cursorId == null || cursorId == 0 ?
//                postQueryRepository.findAllByTitleFirst(title, type, page):
//                postQueryRepository.findAllByTitle(title, type, cursorId, page, postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
//    }


}
