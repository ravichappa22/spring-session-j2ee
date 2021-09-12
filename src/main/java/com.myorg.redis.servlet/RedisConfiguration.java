package com.myorg.redis.servlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@Profile("sessionha")
@Configuration
public class RedisConfiguration {

    @Bean("lettuceConnectionFactory")
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);;
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(springSessionRepositoryFilter());
    }

    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
        List<HttpSessionListener> listeners = new ArrayList<>();
        listeners.add(new CustomSessionListener());
        SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter
                            = new SessionEventHttpSessionListenerAdapter(listeners);
        return  sessionEventHttpSessionListenerAdapter;
    }

    @Bean
    public SessionRepositoryFilter springSessionRepositoryFilter() {
        return new SessionRepositoryFilter(redisIndexedSessionRepository());
    }

    @Bean
    public RedisIndexedSessionRepository redisIndexedSessionRepository() {
        RedisIndexedSessionRepository redisIndexedSessionRepository = new RedisIndexedSessionRepository(sessionObjectRedisOperations());
        redisIndexedSessionRepository.setRedisKeyNamespace("wc-sessions");
        redisIndexedSessionRepository.setFlushMode(FlushMode.IMMEDIATE);
        redisIndexedSessionRepository.setDefaultMaxInactiveInterval(60);
        return redisIndexedSessionRepository;
    }

    @Bean
    public RedisOperations<Object, Object> sessionObjectRedisOperations() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }



}
