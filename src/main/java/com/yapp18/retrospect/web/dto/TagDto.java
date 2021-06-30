package com.yapp18.retrospect.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TagDto {

    // detail 조회 시
    private Long tagIdx;
    private String tag;

    // 메인 조회 시
    @Getter
    @Setter
    public static class FirstTagDto{
        private String tag;
    }

}
