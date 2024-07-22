package com.musinsa.shop.style.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.musinsa.shop.style.cache.LocalCacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LocalCacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(LocalCacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
                                .expireAfterWrite(cache.getDuration())
                                .maximumSize(cache.getMaximumSize())
                                .build()
                        )
                )
                .toList();

        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
