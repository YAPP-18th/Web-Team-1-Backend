package com.yapp18.retrospect.web.controller;


import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.ImageService;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(value = "ImageController") // swagger 리소스 명시
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;
    private final TokenService tokenService;

    @ApiOperation(value = "images", notes = "[이미지] 이미지 s3 업로드")
    @PostMapping("")
    public ResponseEntity<Object> s3UploadImage(@ApiParam(value = "파일 형식 ", required = true)
                                                            HttpServletRequest request,
                                                @RequestPart MultipartFile file){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.IMAGE_UPLOAD.getResponseMessage(),
                imageService.uploadImage(file, userIdx)), HttpStatus.OK);
    }

}
