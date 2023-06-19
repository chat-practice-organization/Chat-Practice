package com.practice.chat.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(basePackages = "com.practice.chat.repository.redis.cache",
        redisTemplateRef = "cacheRedisTemplate", keyValueTemplateRef = "cacheKeyValueTemplate",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class CacheRedisConfig {
    @Value("${redis.cache.host}")
    private String CACHE_HOST;

    @Value("${redis.cache.port}")
    private int CACHE_PORT;


    @Bean(name = "cacheRedisFactory")
    public RedisConnectionFactory cacheRedisFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(CACHE_HOST, CACHE_PORT));
    }

    @Bean(name = "cacheKeyValueTemplate")
    public KeyValueTemplate cacheKeyValueTemplate() {
        RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(
                new RedisKeyValueAdapter(cacheRedisTemplate()),
                new RedisMappingContext()
        );
        return keyValueTemplate;
    }

    @Bean(name = "cacheRedisTemplate")
    public RedisTemplate<String, Object> cacheRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cacheRedisFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
