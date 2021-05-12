package com.yapp18.retrospect.config;

import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2 // swagger ver2 활성화 어노테이션
public class SwaggerConfig {

    @Bean
    public Docket api() {
        // response 값 정의
        int[] status = new int[]{200, 404, 500};
        String[] message = new String[] {"OK!!", "Internal Server Error..", "Not Found"};
        List<ResponseMessage> responseMessages = new ArrayList<>();
        int idx = 0;

        for (String msg: message){
            responseMessages.add(new ResponseMessageBuilder()
            .code(status[idx])
            .message(msg)
            .build());
            idx ++;
        }

        // swagger 정의
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select() //ApiSelectorBuilder를 생성
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/**")) // 그중 /api/** 인 URL들만 필터링
                .build()
                .globalResponseMessage(RequestMethod.GET, responseMessages);
    }



}
