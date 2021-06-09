package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ErrorDefaultResponse<T> {
    @JsonFormat(timezone = "Asia/Seoul")
    private Timestamp timestamp;
    private T error;

    public ErrorDefaultResponse() {
        this.timestamp = null;
        this.error = null;
    }

    public static<T> ErrorDefaultResponse<T> res(final T error){
        return ErrorDefaultResponse.<T>builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .error(error)
                .build();
    }
}
