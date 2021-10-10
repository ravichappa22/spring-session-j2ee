package com.myorg.redis.servlet;

import io.lettuce.core.ReadFrom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.RequestContextFilter;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("sessionha")
@Configuration
public class RedisConfiguration {

    @Value("#{systemProperties['redis.host'] ?: 'localhost'}")
    private String redishost;
    @Value("#{systemProperties['redis.port'] ?: '6379'}")
    private int port;

    @Value("#{systemProperties['redis.cluster'] ?: 'localhost:6379'}")
    private String redisClusterString;

    @Bean("lettuceConnectionFactory")
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redishost);
        redisStandaloneConfiguration.setPort(port);
        LettuceConnectionFactory lettuceConnectionFactory= new LettuceConnectionFactory(redisStandaloneConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        lettuceConnectionFactory.getConnection();
        System.out.println("lettuceConnectionFactory initialized");
        /*RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        redisSentinelConfiguration.master("mymaster")
                                  .sentinel("localhost", 26379)
                                  .sentinel("localhost", 26380);
        LettuceConnectionFactory lettuceConnectionFactory= new LettuceConnectionFactory(redisSentinelConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        lettuceConnectionFactory.getConnection();
        return lettuceConnectionFactory;*/

        /*LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                                                                            .readFrom(ReadFrom.ANY_REPLICA)
                                                                            .build();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new
                    RedisStaticMasterReplicaConfiguration("localhost",6379);


        //this.getSlaves().forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
        staticMasterReplicaConfiguration.addNode("localhost",6380);
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);*/

        /*RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration
             // .clusterNode(new RedisNode("localhost", 7000))
                               //  .clusterNode(new RedisNode("localhost", 7001))
                                 .clusterNode(new RedisNode("localhost", 7002))
                                // .clusterNode(new RedisNode("localhost", 7003))
                                 .clusterNode(new RedisNode("localhost", 7004))
                                 .clusterNode(new RedisNode("localhost", 7005));*/
        //return new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;

    }

    /*@Bean
    @Primary
    public JedisConnectionFactory jedisConnectionFactory() {
     //the redisClusterString should be comma separatee like localhost:7001,localhost:7002
     RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(Arrays.asList(redisClusterString.split(",")));
     return new JedisConnectionFactory(clusterConfig);
    }*/


    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
        List<HttpSessionListener> listeners = new ArrayList<>();
        listeners.add(new CustomSessionListener());
        SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter
                            = new SessionEventHttpSessionListenerAdapter(listeners);
        return  sessionEventHttpSessionListenerAdapter;
    }

   /* @Bean
    public HttpSessionListener httpSessionListener(){
        return new CustomSessionListener();
    }*/

    /**
     * Do not change Bean name as DelegatingFilterproxy looks for this bean
     * @return
     */
    @Bean("springSessionRepositoryFilter")
    public SessionRepositoryFilter springSessionRepositoryFilter() {
        System.out.println("scanning springSessionRepositoryFilter");
        return new SessionRepositoryFilter(redisIndexedSessionRepository());
    }

    @Bean
    public RedisIndexedSessionRepository redisIndexedSessionRepository() {
        RedisIndexedSessionRepository redisIndexedSessionRepository = new RedisIndexedSessionRepository(sessionObjectRedisOperations());
        redisIndexedSessionRepository.setRedisKeyNamespace("wc-sessions");
        redisIndexedSessionRepository.setFlushMode(FlushMode.IMMEDIATE);
        redisIndexedSessionRepository.setDefaultMaxInactiveInterval(900);
        //redisIndexedSessionRepository.setApplicationEventPublisher(new CustomApplicationEventPublisher());
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
