package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.web.dto.ErrorDefaultResponse;
import com.yapp18.retrospect.web.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
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

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> sqlExceptionHandler(SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDefaultResponse.res(
                        ErrorDto.builder()
                                .code("DE" + e.getSQLState())
                                .exception(e.toString().split(":")[0])
                                .message(e.getMessage())
                                .build()
                        )
                );
    }

//    @ExceptionHandler(RuntimeException.class) // RuntimeException으로 뭉뚱그리면 SQLException 까지 포함됨 
//    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDefaultResponse.res(e));
//    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Object> tokenAbsenceExceptionHandler(TokenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDefaultResponse.res(
                        ErrorDto.builder()
                                .code(e.getCode())
                                .exception(e.getException())
                                .message(e.getMessage())
                                .build()
                        )
                );
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> restExceptionHandler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("message", e.getMessage());

        return new ResponseEntity<>(resBody, e.getStatus());
    }

//    @ExceptionHandler(EntityNullException.class)
//    public ResponseEntity<Object> entityNullExceptionHandler(EntityNullException e) {
//        return new ResponseEntity<>(ApiDefaultResponse.res(404, e.getMessage()), e.getStatus());
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public void illegalArgumentExceptionHandler(IllegalArgumentException e) {
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
//    public void oauthAuthenticationProcessingExceptionHandler(OAuth2AuthenticationProcessingException e){
//    }


}
