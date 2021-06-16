package com.yapp18.retrospect.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.recent.RecentLog;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final RedisTemplate<String, PostDto.ListResponse> redisTemplate;

    @Transactional
    public List<PostDto.ListResponse> findAllPostsByUserIdx(Long userIdx){
        return postRepository.findAllByUserUserIdxOrderByCreatedAtDesc(userIdx)
                .stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
    }

    // 최근 읽은 글 저장
    @Transactional
    public void saveRecentReadPosts(Long userIdx, Long postIdx){
        ListOperations<String, PostDto.ListResponse> listOperations = redisTemplate.opsForList();
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
        PostDto.ListResponse postDto = postMapper.postToListResponse(post, userIdx);
        String key = "userIdx::"+ userIdx;
        listOperations.leftPush(key, postDto);
        redisTemplate.expireAt(key, Date.from(ZonedDateTime.now().plusDays(7).toInstant())); // 유효기간 TTL 일주일 설정
    }

    // 최근 읽은 글 조회
    @Transactional
    public List<PostDto.ListResponse> findRecentPosts(Long userIdx) {
        ListOperations<String, PostDto.ListResponse> listOperations = redisTemplate.opsForList();
        String key = "userIdx::" + userIdx;
        long size = listOperations.size(key) == null ? 0 : listOperations.size(key); // NPE 체크해야함.

        return listOperations.range(key, 0, size);

    }

}
