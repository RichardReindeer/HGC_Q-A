package com.bambi.straw.gateway;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class RedisTest {
    //这个RedisTemplate对象是SpringBoot提供的
    //在使用之前，就已经在Spring完成注入了
    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Test
    void setRedisTemplate(){
        //新增信息到redis
        //redisTemplate.opsForValue().set("msg","hello World");

        //获得redis中的信息
        String msg = redisTemplate.opsForValue().get("msg");
        System.out.println("运行完毕");
        System.out.println("msg = " + msg);

        //删除一个保存的信息
        redisTemplate.delete("msg");
        System.out.println("删除信息");
    }
}
