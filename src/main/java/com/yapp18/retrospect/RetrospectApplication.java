package com.yapp18.retrospect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RetrospectApplication {
	public static void main(String[] args) {
		SpringApplication.run(RetrospectApplication.class, args);
	}
}
