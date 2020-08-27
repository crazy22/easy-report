package com.godmk.tool.easyreport.controller;


import com.godmk.tool.easyreport.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * easy_report
 * 模型管理
 *
 * @author crazy22
 */
@Api(value = "Redis", tags = "Redis")
@Controller
@RequestMapping("redis")
public class RedisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);

    @Resource
    RedisUtil redisUtil;

    @RequestMapping(value = "v1/redisSave",method = {RequestMethod.GET})
    @ApiOperation(value = "保存到redis,并返回结果",notes = "保存到redis")
    @ResponseBody
    public Object redisSave(@ApiParam(value = "传入key值") @RequestParam String key,
                            @ApiParam(value = "传入value值") @RequestParam String value){
        redisUtil.set(key,value);
        return redisUtil.get(key);
    }

    @RequestMapping(value = "v1/redisGetAllKeyValue",method = {RequestMethod.GET})
    @ApiOperation(value = "获取redis种所有key和value值",notes = "获取redis种所有key和value值")
    @ResponseBody
    public Object redisGetAllKeyValue(){
        Set<String> keys = redisUtil.keys("*");
        Iterator<String> iterator = keys.iterator();
        Map<String,Object> map = new HashMap<>();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object o = redisUtil.get(key);
            map.put(key,o);
        }
        return map;
    }


    @RequestMapping(value = "v1/deleteRedisAll",method = {RequestMethod.DELETE})
    @ApiOperation(value = "清除redis所有缓存",notes = "清除redis所有缓存")
    @ResponseBody
    public Object deleteRedisAll(){
        Set<String> keys = redisUtil.keys("*");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            redisUtil.del(iterator.next());
        }
        return "删除redis所有数据成功";
    }

    @RequestMapping(value = "v1/getRedisValue",method = {RequestMethod.GET})
    @ApiOperation(value = "根据key获取value值",notes = "根据key获取value值")
    @ResponseBody
    public Object getRedisValue(@ApiParam(value = "传入key值") @RequestParam String key){
        return redisUtil.get(key);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "redis-发布", notes = "redis-发布")
    @RequestMapping(value = "v2/sendMessage", method = RequestMethod.GET)
    @ResponseBody
    public void testPush(@ApiParam(value = "发布的消息内容", required = true) @RequestParam("body") String body){
        /**
         * 使用redisTemplate的convertAndSend()函数，
         * String channel, Object message
         * channel代表管道，
         * message代表发送的信息
         */
        System.out.println("body==="+body);
        stringRedisTemplate.convertAndSend("topic1", body);
    }
}
