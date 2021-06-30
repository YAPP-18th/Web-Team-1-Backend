package com.yapp18.retrospect.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiPagingResultResponse<T> {
    // 메인화면 등 목록 조회 시
    private boolean isNext;
    private List<T> result;

    public ApiPagingResultResponse(boolean isNext,List<T> result){
        this.isNext = isNext;
        this.result = result;
    }
}