package com.yapp18.retrospect.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ApiPagingResultResponse<T> {
    private boolean isNext;
    private List<T> result;

    public ApiPagingResultResponse(boolean isNext, List<T> result){
        this.isNext = isNext;
        this.result = result;
    }
}