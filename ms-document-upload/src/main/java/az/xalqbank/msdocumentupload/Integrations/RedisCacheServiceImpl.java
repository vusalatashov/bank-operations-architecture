package az.xalqbank.msdocumentupload.Integrations.impl;

import az.xalqbank.msdocumentupload.Integrations.RedisCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void cacheData(String key, Object data, Duration ttl) {
        redisTemplate.opsForValue().set(key, data, ttl);
    }
}
