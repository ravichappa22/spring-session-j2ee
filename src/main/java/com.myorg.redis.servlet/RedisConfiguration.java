package com.myorg.redis.servlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;


@Configuration
public class RedisConfiguration {

    /**
     * Lettuce
     */
    @Bean("lettuceConnectionFactory")
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6380);
        // redisStandaloneConfiguration.setUsername("redis");
        //redisStandaloneConfiguration.setPassword(RedisPassword.of("laf494rjlgf0445"));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

}
