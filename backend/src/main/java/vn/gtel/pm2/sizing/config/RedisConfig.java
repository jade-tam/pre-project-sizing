package vn.gtel.pm2.sizing.config;

import org.springframework.cache.annotation.EnableCaching;
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
public class RedisConfig {

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
}