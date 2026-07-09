package vn.gtel.pm2.sizing.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import vn.gtel.pm2.sizing.exception.TooManyRequestsException;

import java.time.Duration;

@Component
public class RateLimiter {

    private final StringRedisTemplate redis;

    public RateLimiter(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void check(String key,
                      int limit,
                      Duration window) {

        Long count = redis.opsForValue().increment(key);

        if (count == 1) {
            redis.expire(key, window);
        }

        if (count > limit) {
            throw new TooManyRequestsException();
        }
    }
}