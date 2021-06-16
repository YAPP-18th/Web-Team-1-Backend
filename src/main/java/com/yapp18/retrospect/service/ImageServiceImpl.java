package com.yapp18.retrospect.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ImageServiceImpl implements s3Service{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")  // 프로퍼티에서 cloude.aws.s3.bucket에 대한 정보를 불러옵니다.
    public String bucket;

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream ,objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @Override
    public List<String> getFileList(String filePath) {
        return amazonS3Client.listObjects(bucket, filePath).getObjectSummaries()
                .stream().map(x-> getFileUrl(x.getKey())).collect(Collectors.toList());
    }

    @Override
    public List<String> getKeyList(String filePath) {
        return amazonS3Client.listObjects(bucket, filePath).getObjectSummaries()
                .stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }


    @Override
    public void deleteFileList(List<String> garbage) {
        if (!garbage.isEmpty()){
            List<DeleteObjectsRequest.KeyVersion> objects = garbage.stream()
                    .map(DeleteObjectsRequest.KeyVersion::new)
                    .collect(Collectors.toList());
            // 지워야할 객체요청을 새로 만든다
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
            deleteObjectsRequest.setKeys(objects);
            // 임시저장한 s3 삭제
            DeleteObjectsResult result = amazonS3Client.deleteObjects(deleteObjectsRequest);
        }
    }
}
