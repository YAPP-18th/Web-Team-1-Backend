package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.TemplateService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(value = "TemplateController")
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;
    private final TokenService tokenService;

    @ApiOperation(value = "template", notes = "[템플릿] 템플릿 양식 불러오기")
    @GetMapping("/{templateIdx}")
    public ResponseEntity<Object> getTemplate(@ApiParam(value = "templateIdx")
                                              @PathVariable(value = "templateIdx") Long templateIdx){
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.TEMPLATE_FIND.getResponseMessage(),
                templateService.getTemplateContents(templateIdx)), HttpStatus.OK);
    }
}
