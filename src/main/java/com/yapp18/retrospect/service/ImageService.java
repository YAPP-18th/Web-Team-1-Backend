package com.yapp18.retrospect.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.image.ImageRepository;
import com.yapp18.retrospect.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        List<String> garbage = test(imageList, s3Service.getKeyList(filepath)); // 이미지 리스트에 없는 s3 가비지 데이터 추출.
        // 객체 url의 모음이지 key 모음은 아님.
        s3Service.deleteFileList(garbage); // 삭제
    }

    // imageList에만 있는 것 db에 저장하기
    public void updateNewImages(List<String> newList, List<String> dbImageList, Post post){
        System.out.println("db에는 없는데 imageList에는 있다."+compareImageList(dbImageList, newList));
        if (!compareImageList(dbImageList, newList).isEmpty()){
            for (String url : compareImageList(dbImageList, newList)) {
                imageRepository.save(Image.builder().imageUrl(url).post(post).build());
            }
        }
    }

    // imageList에는 없고 db에만 존재하면 삭제
    public void deleteDbImage(List<String> newList, List<String> dbImageList){
        System.out.println("db에는 있는데, imageList에 없다."+ compareImageList(newList,dbImageList));
        if (!compareImageList(newList,dbImageList).isEmpty()){
            imageRepository.deleteByImageUrlInQuery(compareImageList(newList, dbImageList));
        }
    }


    // file 이름 지정
    @SneakyThrows
    private String createFileName(String originFileName, Long userIdx) {
        // images 폴더에 아이디 폴더의 post 폴더의.... originFilename + 랜덤 string?
        return "images"+"/"+userIdx+"/"+"posts"+"/"+ originFileName +userIdx;
    }

    private List<String> compareImageList(List<String> imageList, List<String> compareImageList){
        // 2번째 리스트에만 있는 값 필터링
        return compareImageList.stream().filter(x -> !imageList.contains(x))
                .collect(Collectors.toList());
    }

    private List<String> test(List<String> imageList, List<String> compareImageList){
        List<String> encode = imageList.stream().map(x-> {
            try {
                return URLDecoder.decode(x,"UTF-8").replace("https://s3doraboda.s3.ap-northeast-2.amazonaws.com/","");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return x;
        }).collect(Collectors.toList());
        return compareImageList.stream().filter(x -> !encode.contains(x))
                .collect(Collectors.toList());
    }



}
