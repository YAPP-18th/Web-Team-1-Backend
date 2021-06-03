package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;

    @Transactional
    public List<PostDto.ListResponse> findAllPostsByUserIdx(Long userIdx){

        return postRepository.findAllByUserUserIdxOrderByCreatedAtDesc(userIdx)
                .stream().map(postMapper::postToListResponse)
                .collect(Collectors.toList());
    }

}
