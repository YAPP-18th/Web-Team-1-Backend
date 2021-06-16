package com.yapp18.retrospect.domain.recent;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("userIdx")
@Getter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class RecentLog {

    @Id
    private  Long userIdx;
    private  PostDto.ListResponse postDto;

}
