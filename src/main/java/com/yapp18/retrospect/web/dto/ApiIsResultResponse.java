package com.yapp18.retrospect.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiIsResultResponse<T> {
    private boolean isWriter; // 내가 작성자인지?
    private boolean isScrap;
    private  T result;

    public ApiIsResultResponse(boolean isWriter, boolean isScrap, T result){
        this.isWriter = isWriter;
        this.isScrap = isScrap;
        this.result = result;

    }

}
