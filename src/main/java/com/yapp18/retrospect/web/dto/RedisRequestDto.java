package com.yapp18.retrospect.web.dto;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.recent.RecentLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RedisRequestDto {

    private Long userIdx;
    private Post post;

//    @Builder
//    public RedisRequestDto(Long userIdx, Post post){
//        this.userIdx = userIdx;
//        this.post = post;
//    }
//
//    public RecentLog toRedisHash(){
//        return RecentLog.builder()
//                .userIdx(userIdx)
//                .post(post)
//                .build();
//    }


}
