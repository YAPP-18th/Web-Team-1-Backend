package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.recent.RecentLog;
import com.yapp18.retrospect.domain.recent.RecentRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.RedisRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final RecentRepository recentRepository;

    private final RedisTemplate<Long, Object> redisTemplate;

    @Transactional
    public List<PostDto.ListResponse> findAllPostsByUserIdx(Long userIdx){
        return postRepository.findAllByUserUserIdxOrderByCreatedAtDesc(userIdx)
                .stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
    }

    // 최근 읽은 글 저장
    @Transactional
    public void saveRecentReadPosts(Long userIdx, Long postIdx){
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
        PostDto.ListResponse postDto = postMapper.postToListResponse(post, userIdx);
        RecentLog result = RecentLog.builder().userIdx(userIdx).postDto(postDto).build();
        System.out.println("--->>>"+ result.getPostDto());
        Long id = recentRepository.save(result).getUserIdx();
        System.out.println("--->>>>"+ id);
    }

    // 최근 읽은 글 조회
    @Transactional
    public RecentLog findRecentPosts(Long userIdx){
        return recentRepository.findById(userIdx).orElseThrow(() -> new IllegalArgumentException(".///////"));
    }

}
