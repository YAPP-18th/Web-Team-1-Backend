package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.exception.OAuth2AuthenticationProcessingException;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.ErrorDefaultResponse;
import com.yapp18.retrospect.web.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
//    @ExceptionHandler(EntityNullException.class)
//    public ResponseEntity<Object> entityNullExceptionHandler(EntityNullException e) {
//        return new ResponseEntity<>(ApiDefaultResponse.res(404, e.getMessage()), e.getStatus());
//    }

    @ExceptionHandler(EntityNullException.class)
    public ResponseEntity<Object> entityNullExceptionHandler(EntityNullException e) {
        return new ResponseEntity<>(ErrorDefaultResponse.res(
                ErrorDto.builder()
                .code(e.getCode())
                .exception(e.toString().split(":")[0])
                .message(e.getMessage())
                .build()
        ), HttpStatus.NOT_FOUND);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public void illegalArgumentExceptionHandler(IllegalArgumentException e) {
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
//    public void oauthAuthenticationProcessingExceptionHandler(OAuth2AuthenticationProcessingException e){
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RuntimeException.class)
//    public void runtimeExceptionHandler(RuntimeException e) {
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return new ResponseEntity<>(ErrorDefaultResponse.res(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> restExceptionHandler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("message", e.getMessage());

        return new ResponseEntity<>(resBody, e.getStatus());
    }
}
