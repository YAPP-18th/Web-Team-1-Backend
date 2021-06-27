package com.yapp18.retrospect.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.recent.RecentLog;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.*;
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
    public List<PostDto.ListResponse> findRecentPosts(Long userIdx, Long page, Integer pageSize) {
        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
        // 페이징 start, end 인덱스
        System.out.println("테스트 ->>>"+ zSetOps.getOperations());
        HashMap<String, Integer> pagingIdx = getPagingIndex(page, pageSize);
        System.out.println("시작"+ pagingIdx.get("start") + " 끝 "+ pagingIdx.get("end"));
        // linkedHashMap 으로 저장된 redis 값들을 List로 변환해줌
//        long size = zSetOps.size(setKey(userIdx));
        ObjectMapper objectMapper = new ObjectMapper();
        List<RecentLog> result = objectMapper.convertValue(Objects.requireNonNull(zSetOps.reverseRange(setKey(userIdx),
                pagingIdx.get("start"),
                pagingIdx.get("end"))),
                new TypeReference<List<RecentLog>>() {
        });

        // if post가 존재하는 경우만 filter
        List<RecentLog> recentLogList = result.stream().filter(x -> postRepository.findById(x.getPostIdx()).isPresent())
                .collect(Collectors.toList());
        return recentLogList.stream().map(x -> postMapper.postToListResponse(findPostById(x.getPostIdx()), userIdx)).collect(Collectors.toList());


    }
    // redis 키 여부 체크
    private boolean isKey(String key){
        return redisTemplate.hasKey(key);
    }

    // redis key String 생성
    private String setKey(Long userIdx){
        return "userIdx::"+userIdx;
    }

    // postIdx로 post 엔티티 찾기
    private Post findPostById(Long postIdx){
        return postRepository.findById(postIdx).get();
    }

    // redis에서 value 삭제
//    public void deleteRedisPost(Long userIdx,Long postIdx){
//        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
//        RecentLog recentLog = RecentLog.builder().userIdx(userIdx).postIdx(postIdx).build();
//        System.out.println("삭제됨!");
//        zSetOps.remove(setKey(userIdx), recentLog);
//    }

    // redis에 해당 postIdx 있는지 확인
    public boolean isPostsExist(Long userIdx, Long postIdx){
        ZSetOperations<String, RecentLog> zSetOps = redisTemplate.opsForZSet();
        System.out.println("redis 전체?" + zSetOps);
        RecentLog recentLog = RecentLog.builder().userIdx(userIdx).postIdx(postIdx).build();
        ObjectMapper objectMapper = new ObjectMapper();
        List<RecentLog> result = objectMapper.convertValue(Objects.requireNonNull(zSetOps.reverseRange(setKey(userIdx), 0, -1)), new TypeReference<List<RecentLog>>() {});
        return result.contains(recentLog);
    }

    // redis 결과 페이징
    private HashMap<String,Integer> getPagingIndex(Long page, Integer pageSize){
        System.out.println("pageable 정보:" + page + "//" + pageSize);
        int start = (int) (page * pageSize); // page, pageSize
        HashMap<String, Integer> map = new HashMap<>();
        map.put("start", start);
        map.put("end", start + pageSize -1);
        return map;
    }

    // 페이징 시 다음 결과 있는지 검사
    private boolean isNextRedis(long size, Long page ,Integer pageSize){
        return page + 1 <= Math.round(size / pageSize);
    }
}
