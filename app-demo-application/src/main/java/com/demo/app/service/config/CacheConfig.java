package com.demo.app.service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;


/**
 * The type Cache config.
 */
@Configuration
@EnableCaching
public class CacheConfig {


    /**
     * Custom caffeine cache.
     *
     * @return the caffeine cache
     */
    @Bean
    public CaffeineCache custom() {
        return new CaffeineCache("account-normal", Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .initialCapacity(100)
                .maximumSize(1000)
                .recordStats()
                .build());
    }


    /**
     * Cache manager cache manager.
     *
     * @param caches the caches
     * @return the cache manager
     */
    @Bean
    public CacheManager caffeineCacheManager(List<CaffeineCache> caches) {
        var manager = new SimpleCacheManager();
        manager.setCaches(caches);
        return manager;
    }
}
