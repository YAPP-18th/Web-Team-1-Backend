package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.BannerService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(value = "AuthController")
@RequestMapping("/api/v1/banner")
public class BannerController {

    private final BannerService bannerService;

    @ApiOperation(value = "banner", notes = "[배너] 조건에 맞는 양식 조회 ")
    @GetMapping("")
    public ResponseEntity<Object> getRecommendBanner(@ApiParam(value = "개인/팀 ", required = true, example = "personal")
                                                     @RequestParam(value = "project") String project,
                                                     @ApiParam(value = "감정 유무 ", required = true, example = "1")
                                                     @RequestParam(value = "emotion") int emotion,
                                                     @ApiParam(value = "회고 시기 ", required = true, example = "now")
                                                     @RequestParam(value = "term") String term
    ){
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.BANNER_GET.getResponseMessage(),
                bannerService.getRecommendBanner(project, emotion, term)), HttpStatus.OK);
    }
}
