package com.genesys.resiliency.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class QueueConfig {
    @Bean
    public CaffeineCache getQueueCache() {
        return new CaffeineCache("queueCache",
                Caffeine.newBuilder()
                        .expireAfterAccess(1, TimeUnit.DAYS)
                        .build());
    }

    @Bean
    public CaffeineCache getQueueMetricCache() {
        return new CaffeineCache("queueMetricCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .recordStats()
                        .build());
    }
}
