package com.yapp18.retrospect.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.search.SearchTerm;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostService postService;

    // 제목으로 검색
    @Transactional
    public List<SearchDto.SearchListResponse> findPostsByTitle(String title){
        List<SearchDto.ListResponse> result = postQueryRepository.findAllByTitle(title);
        List<SearchDto.SearchListResponse> test = new ArrayList<>();
        for (SearchDto.ListResponse res: result){
            test.add(new SearchDto.SearchListResponse(res, tagRepository.findByPostPostIdx(res.getPostIdx())
                    .stream()
                    .map(Tag::getTag)
                    .collect(Collectors.toList())));
        }
        return test;
    }


}
