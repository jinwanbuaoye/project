package com.example.lotterysystem;

import com.example.lotterysystem.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest() {
        redisTemplate.opsForValue().set("key1", "value1");
        String value = redisTemplate.opsForValue().get("key1");
        System.out.println(value);
//redisTemplate.delete("key");
    }

    @Test
    void setRedisUtil(){
        redisUtil.set("key2", "value2");
        redisUtil.set("key3", "value3",60L);
        System.out.println("has key2:"+redisUtil.hasKey("key2") );
        System.out.println("has key3:"+redisUtil.hasKey("key3") );
        System.out.println("has key2:"+redisUtil.get("key2") );
        System.out.println("has key3:"+redisUtil.get("key3") );
        redisUtil.del("key2");
        System.out.println("has key2:"+redisUtil.get("key2") );
        System.out.println("has key3:"+redisUtil.get("key3") );
    }
}