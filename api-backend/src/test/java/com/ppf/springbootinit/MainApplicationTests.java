package com.ppf.springbootinit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 主类测试
 *

 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.delete("redis");
    }

}
