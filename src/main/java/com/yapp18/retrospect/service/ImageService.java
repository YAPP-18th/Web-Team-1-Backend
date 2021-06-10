package com.yapp18.retrospect.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final s3Service s3Service;

    // s3 이미지 업로드
    @Transactional
    public String uploadImage(MultipartFile file, Long userIdx){
        // file name null 처리
        String fileName = file.getOriginalFilename() == null ? UUID.randomUUID().toString() : file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        System.out.println("파일 이름: "+createFileName(fileName, userIdx));
        try {
            s3Service.uploadFile(file.getInputStream(), objectMetadata, createFileName(fileName, userIdx));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return s3Service.getFileUrl(createFileName(fileName, userIdx));
    }
    // s3 이미지 삭제

    // file 이름 지정
    private String createFileName(String originFileName, Long userIdx){
        // images 폴더에 아이디 폴더의 post 폴더의.... originFilename + 랜덤 string?
        return "images"+"/"+userIdx+"/"+"posts"+"/"+originFileName+UUID.randomUUID().toString();
    }

}
