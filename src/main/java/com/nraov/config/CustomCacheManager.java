package com.nraov.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;


@Slf4j
public class CustomCacheManager extends RedisCacheManager {

    public static final String SUPER_ADMIN_TENANT = "SUPER_ADMIN_TENANT";

    public CustomCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        RedisCacheManager.builder()
                .cacheWriter(cacheWriter)
                .cacheDefaults(defaultCacheConfiguration)
                .build();
    }

    /**
     * @param name
     * @return Prefix the cache store name with the TENANT KEY
     * For SUPER ADMIN no prefix applied
     */
    @Override
    public Cache getCache(String name) {
        log.info("Inside getCache:" + name);
        String tenantId = TenantContext.getTenant().get();
        log.info("Use Tenant:-" + tenantId);
        if (SUPER_ADMIN_TENANT.equals(tenantId)) {
            return super.getCache(name);
        } else if (name.startsWith(tenantId)) {
            return super.getCache(name);
        }
        return super.getCache(tenantId + "_" + name);
    }
}
