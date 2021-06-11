package com.yapp18.retrospect.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.image.ImageRepository;
import com.yapp18.retrospect.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final s3Service s3Service;
    private final ImageRepository imageRepository;


    // s3 이미지 업로드
    @Transactional
    public String uploadImage(MultipartFile file, Long userIdx){
        // file name null 처리
        String fileName = file.getOriginalFilename() == null ? UUID.randomUUID().toString() : file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        try {
            s3Service.uploadFile(file.getInputStream(), objectMetadata, createFileName(fileName, userIdx));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return s3Service.getFileUrl(createFileName(fileName, userIdx));
    }
    // s3 이미지 목록 가져오기
    @Transactional
    public List<String>  getFileList(Long userIdx){
        String filepath = "images"+"/"+ userIdx+ "/" +"posts"+"/";
        return s3Service.getFileList(filepath);
    }

    // s3 이미지 목록 삭제
    @Transactional
    public void deleteImageList(List<String> imageList, Long userIdx){
        String filepath = "images"+"/"+ userIdx+ "/" +"posts"+"/";
        List<String> garbage = compareImageList(imageList,s3Service.getFileList(filepath)); // 이미지 리스트에 없는 s3 가비지 데이터 추출.
        s3Service.deleteFileList(garbage); // 삭제
    }

    // imageList에만 있는 것 db에 저장하기
    public void updateNewImages(List<String> newList, List<String> dbImageList, Post post){
        System.out.println("db에는 없는데 imageList에는 있다."+compareImageList(dbImageList, newList));
        if (!compareImageList(newList, dbImageList).isEmpty()){
            for (String url : compareImageList(dbImageList, newList)) {
                imageRepository.save(Image.builder().imageUrl(url).post(post).build());
            }
        }
    }

    // imageList에는 있고 db에만 존재하면 삭제
    public void deleteDbImage(List<String> newList, List<String> dbImageList){
        System.out.println("db에는 있는데, imageList에 없다."+ compareImageList(newList,dbImageList));
        if (!compareImageList(dbImageList,newList).isEmpty()){
            imageRepository.deleteByImageUrlInQuery(compareImageList(newList, dbImageList));
        }
    }


    // file 이름 지정
    private String createFileName(String originFileName, Long userIdx){
        // images 폴더에 아이디 폴더의 post 폴더의.... originFilename + 랜덤 string?
        return "images"+"/"+userIdx+"/"+"posts"+"/"+originFileName+UUID.randomUUID().toString();
    }

    //  imageList에 없는 가비지 데이터들을 s3에서 제거
    private List<String> compareImageList(List<String> imageList, List<String> compareImageList){
        return compareImageList.stream().filter(x -> !imageList.contains(x.replace("https://s3doraboda.s3.ap-northeast-2.amazonaws.com/","")))
                .collect(Collectors.toList());
    }


}
