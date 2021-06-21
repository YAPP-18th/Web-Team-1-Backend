package com.yapp18.retrospect.web.dto;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.recent.RecentLog;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class RedisRequestDto {

    private Long userIdx;
    private Long postIdx;
//    private Long timestamp;

    @Builder
    public RedisRequestDto(Long userIdx, Long postIdx){
        this.userIdx = userIdx;
        this.postIdx = postIdx;
//        this.timestamp = timestamp;
    }

//    public RecentLog toRedisHash(){
//        return RecentLog.builder()
//                .userIdx(userIdx)
//                .post(post)
//                .build();
//    }
//

}
