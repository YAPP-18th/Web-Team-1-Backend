package com.yapp18.retrospect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2 // swagger ver2 활성화 어노테이션
public class SwaggerConfig {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    private String googleTokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
    private String googleTokenRequestEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenEndpoint;
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoTokenRequestEndpoint;

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
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(securityScheme(), apiKey()))
                .globalResponseMessage(RequestMethod.GET, responseMessages);
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .scopeSeparator(",")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(kakaoTokenEndpoint, "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(kakaoTokenRequestEndpoint, kakaoClientId, kakaoClientSecret))
                .build();
        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }
    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("account_email", "email"),
                new AuthorizationScope("profile", "profile")
        };
        return scopes;
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(Arrays.asList(defaultAuth(), new SecurityReference("kakao_oauth(not required)", scopes()))).forPaths(PathSelectors.any()).build();
    }

    private SecurityReference defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return new SecurityReference("Authorization", authorizationScopes);
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}
