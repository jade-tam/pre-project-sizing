package vn.gtel.pm2.sizing.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import vn.gtel.pm2.sizing.exception.TooManyRequestsException;

import java.time.Duration;

@Component
@Slf4j
public class RateLimiter {

    private final StringRedisTemplate redis;

    public RateLimiter(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void check(String key,
                      int limit,
                      Duration window) {

        try {
            Long count = redis.opsForValue().increment(key);

            if (count == 1) {
                redis.expire(key, window);
            }

            if (count > limit) {
                throw new TooManyRequestsException();
            }

        } catch (RedisConnectionFailureException e) {
            log.warn("Redis unavailable, skipping rate limit");

            // allow request
        }
    }
}