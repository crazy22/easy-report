package com.godmk.tool.easyreport.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author crazy22
 */
@Component
public class RedisTools {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public void testAdd() {
        redisTemplate.opsForValue().set("test2", "测试数据2");
        System.out.println("添加成功");
    }

    public String testGet() {
        String test = redisTemplate.opsForValue().get("test");
        test = test + "+++ " + redisTemplate.opsForValue().get("test2");
        System.out.println(test);
        return test;
    }
}
