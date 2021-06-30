package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.web.advice.EntityNullException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorDto {
    @ApiModelProperty(value = "에러 코드")
    private String code;
    @ApiModelProperty(value = "예외 종류")
    private String exception;
    @ApiModelProperty(value = "에러 메시지")
    private String message;

    @Builder
    public ErrorDto(String code, String exception, String message) {
        this.code = code;
        this.exception = exception;
        this.message = message;
    }
}
