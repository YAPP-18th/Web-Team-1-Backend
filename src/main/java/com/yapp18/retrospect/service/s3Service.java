package com.yapp18.retrospect.service;


import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

// 두 클래스가 긴밀하게 연결되지 않도록 중간에서 느슨하게 연결관계를 맺어준다.
public interface s3Service {

    void uploadFile(InputStream inputStream,
                    ObjectMetadata objectMetadata,
                    String file);
    String getFileUrl(String file);
//    void deleteFileUrl(String file);

}
