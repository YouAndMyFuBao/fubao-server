package com.fubao.project.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;
    public void setStringData(String key, String value,Duration expireTime){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value,expireTime);
    }
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void incrementValue(String key, Long love) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.increment(key,love);
    }
}