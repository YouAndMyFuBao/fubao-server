package com.fubao.project.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
}