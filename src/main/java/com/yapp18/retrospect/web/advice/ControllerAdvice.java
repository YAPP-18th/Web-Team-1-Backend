package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        return new ResponseEntity<>(ApiDefaultResponse.res(404, e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> restExceptionHandler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("message", e.getMessage());

        return new ResponseEntity<>(resBody, e.getStatus());
    }
}
