package com.yapp18.retrospect.web.advice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private String message;

    @Builder
    public RestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
