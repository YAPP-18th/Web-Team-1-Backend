package com.yapp18.retrospect.config;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 502 nginx 에러
    @ResponseStatus(value=HttpStatus.BAD_GATEWAY, reason="Bad Gateway")
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGateWayException(Exception e) {
        return new ResponseEntity<>(ErrorMessage.BAD_GATEWAY, HttpStatus.BAD_GATEWAY);
    }

    // 지원하지 않은 메소드 호출 에러
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(ErrorMessage.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
