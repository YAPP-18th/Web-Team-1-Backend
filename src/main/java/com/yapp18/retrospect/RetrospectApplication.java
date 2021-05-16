package com.yapp18.retrospect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing    //JPA Auditing 활성화
@SpringBootApplication
public class RetrospectApplication {
	public static void main(String[] args) {
		SpringApplication.run(RetrospectApplication.class, args);
	}

}
