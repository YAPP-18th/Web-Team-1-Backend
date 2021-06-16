package com.yapp18.retrospect;

import com.yapp18.retrospect.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing    //JPA Auditing 활성화
// 프로젝트에 AppProperties를 사용할 수 있도록 선언
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class RetrospectApplication {
	public static void main(String[] args) {
		SpringApplication.run(RetrospectApplication.class, args);
	}
}
