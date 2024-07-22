package com.musinsa.shop.style.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum LocalCacheType {
    CATEGORIES("categories", Duration.ofDays(1), 10);

    private final String cacheName;
    private final Duration duration;
    private final int maximumSize;
}