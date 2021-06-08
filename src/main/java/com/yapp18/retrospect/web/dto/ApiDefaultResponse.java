package com.yapp18.retrospect.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ApiDefaultResponse<T> {
    private int statusCode;
//    private HttpStatus status;
    private String responseMessage;
    private T data;

    public ApiDefaultResponse(final int statusCode, final String responseMessage){
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

//    public static<T> ApiDefaultResponse<T> res(final int statusCode, final String responseMessage) {
//        return res(statusCode, responseMessage);
//    }

//    public static<T> ApiDefaultResponse<T> res(final HttpStatus status, final String responseMessage) {
//        return res(status, responseMessage);
//    }

    public static<T> ApiDefaultResponse<T> res(final int statusCode, final String responseMessage, final T t){
        return ApiDefaultResponse.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }

    // state, message만 반환
    public static<T> ApiDefaultResponse<T> res(final int statusCode, final String responseMessage){
        return ApiDefaultResponse.<T>builder()
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}
