package com.yapp18.retrospect.domain.recent;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("userIdx")
@Getter
public class RecentLog {

    @Id
    private final Long userIdx;
    private final PostDto.ListResponse postDto;

    @Builder
    public RecentLog(Long userIdx, PostDto.ListResponse postDto){
        this.userIdx = userIdx;
        this.postDto = postDto;
    }
}
