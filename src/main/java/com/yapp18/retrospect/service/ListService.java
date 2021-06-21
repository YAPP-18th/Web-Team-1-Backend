package com.yapp18.retrospect.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.recent.RecentLog;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.RedisRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final RedisTemplate<String, RecentLog> redisTemplate;

    @Transactional
    public List<PostDto.ListResponse> findAllPostsByUserIdx(Long userIdx, Pageable pageable){
        return postRepository.findAllByUserUserIdxOrderByCreatedAtDesc(userIdx, pageable)
                .stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
    }

    // 최근 읽은 글 저장 -> set으로 변경, postIdx?
    @Transactional
    public void saveRecentReadPosts(Long userIdx, Long postIdx){
        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
        RecentLog recentLog = RecentLog.builder().userIdx(userIdx).postIdx(postIdx).build();
        zSetOps.add(setKey(userIdx), recentLog, new java.util.Date().getTime()); // score은 타임스탬프(최신 읽은 순대로 정렬위해)
        redisTemplate.expireAt(setKey(userIdx), Date.from(ZonedDateTime.now().plusDays(7).toInstant())); // 유효기간 TTL 일주일 설정
    }

    // 최근 읽은 글 조회
    @Transactional
    public List<PostDto.ListResponse> findRecentPosts(Long userIdx) {
        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
        // if redis에 key가 있으면
        ObjectMapper objectMapper = new ObjectMapper(); // linkedHashMap으로 저장된 redis 값들을 List로 변환해줌
        List<RecentLog> result = objectMapper.convertValue(Objects.requireNonNull(zSetOps.reverseRange(setKey(userIdx), 0, -1)),
                new TypeReference<List<RecentLog>>() {
        });
        return result.stream().map(x -> postMapper.postToListResponse(findPostById(x.getPostIdx()), userIdx)).collect(Collectors.toList());

    }
    // redis 키 여부 체크
//    private boolean isKey(String key){
//
//    }
    // redis key 생성
    private String setKey(Long userIdx){
        return "userIdx::"+userIdx;
    }

    // postIdx로 post 엔티티 찾기
    private Post findPostById(Long postIdx){
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
    }

    // redis에서 value 삭제
    public void deleteRedisPost(Long userIdx,Long postIdx){
        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
        RecentLog recentLog = RecentLog.builder().userIdx(userIdx).postIdx(postIdx).build();
        zSetOps.remove(setKey(userIdx), recentLog);
    }

}
