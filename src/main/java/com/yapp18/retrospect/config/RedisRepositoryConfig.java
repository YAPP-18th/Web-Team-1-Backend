package com.yapp18.retrospect.config;


import com.yapp18.retrospect.domain.recent.RecentLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.*;


@Configuration
@EnableRedisRepositories(basePackages = "com.yapp18.retrospect.config")
public class RedisRepositoryConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;


    // redisConnectionFactory 를 통해 외부 redis 를 연결합니다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    //RedisTemplate을 통해 RedisConnection에서 넘겨준 byte 값을 객체 직렬화합니다.
    @Bean
    public RedisTemplate<?,?> redisTemplate(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
