package vn.gtel.pm2.sizing.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import vn.gtel.pm2.sizing.constant.CacheNames;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig implements CachingConfigurer {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory) {


        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        // Disable caching null
                        .disableCachingNullValues()
                        // Every @Cacheable cache entry now lives for 30 mins
                        .entryTtl(Duration.ofMinutes(30))
                        // Make redis stores JSON instead of Java binary data
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                GenericJacksonJsonRedisSerializer.builder()
                                                        .enableUnsafeDefaultTyping()
                                                        .build()
                                        )
                        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)

                // Different TTL for different caches
                .withCacheConfiguration(
                        CacheNames.PROJECTS,
                        defaultConfig.entryTtl(Duration.ofHours(6))
                )
                .withCacheConfiguration(
                        CacheNames.CATALOG_COMPONENTS,
                        defaultConfig.entryTtl(Duration.ofDays(1))
                )
                .withCacheConfiguration(
                        CacheNames.USERS,
                        defaultConfig.entryTtl(Duration.ofMinutes(15))
                )

                .build();
    }

    @Override
    public @Nullable CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {

            @Override
            public void handleCacheGetError(
                    RuntimeException exception,
                    Cache cache,
                    Object key) {

                log.warn(
                        "Redis cache GET failed. cache={}, key={}, error={}",
                        cache.getName(),
                        key,
                        exception.getMessage()
                );
            }

            @Override
            public void handleCachePutError(
                    RuntimeException exception,
                    Cache cache,
                    Object key,
                    Object value) {

                log.warn(
                        "Redis cache PUT failed. cache={}, key={}, error={}",
                        cache.getName(),
                        key,
                        exception.getMessage()
                );
            }

            @Override
            public void handleCacheEvictError(
                    RuntimeException exception,
                    Cache cache,
                    Object key) {

                log.warn(
                        "Redis cache EVICT failed. cache={}, key={}, error={}",
                        cache.getName(),
                        key,
                        exception.getMessage()
                );
            }

            @Override
            public void handleCacheClearError(
                    RuntimeException exception,
                    Cache cache) {

                log.warn(
                        "Redis cache CLEAR failed. cache={}, error={}",
                        cache.getName(),
                        exception.getMessage()
                );
            }
        };
    }
}