package com.nraov.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
public class CacheService {
    @Autowired
    private CacheManager cacheManager;

    public Collection<String> getCacheNames() {
        return this.cacheManager.getCacheNames();
    }

    /**
     * Use by System Admin for cleaning up all the cache entries
     */
    public void evictAll() {
        log.info("Admin Service-Clear all Cache");
        log.info(this.cacheManager.getCacheNames().toString());
        this.cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(this.cacheManager.getCache(cacheName)).clear());
    }
}
