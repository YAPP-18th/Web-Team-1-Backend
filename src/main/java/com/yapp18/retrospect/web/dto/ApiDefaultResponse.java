package com.yapp18.retrospect.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiDefaultResponse<T> {
    private int statusCode;
    private String responseMessage;
    private T data;

    public ApiDefaultResponse(final int statusCode, final String responseMessage){
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    // state, message만 반환
    public static<T> ApiDefaultResponse<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage);
    }

    public static<T> ApiDefaultResponse<T> res(final int statusCode, final String responseMessage, final T t){
        return ApiDefaultResponse.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }

}
