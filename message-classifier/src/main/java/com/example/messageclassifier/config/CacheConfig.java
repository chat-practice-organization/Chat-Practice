package com.example.messageclassifier.config;

import java.beans.BeanProperty;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.concurrent.ConcurrentMapCache;

@Configuration
@ComponentScan({"config"})
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Primary
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(name) {
                    @Override
                    public ValueWrapper get(Object key) {
                        ValueWrapper valueWrapper = super.get(key);
                        if (valueWrapper == null) {
                            logger.info("Cache MISS for key: {}", key);
                        } else {
                            logger.info("Cache HIT for key: {}", key);
                        }
                        return valueWrapper;
                    }
                };
            }
        };
    }
}