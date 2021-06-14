package com.yapp18.retrospect.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class SearchDto {


    private String type;
    private String query;
    private String keyword;
    private Long page;
    private Integer pageSize;

}
