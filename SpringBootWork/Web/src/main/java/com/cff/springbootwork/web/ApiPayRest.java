package com.cff.springbootwork.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/pay")
public class ApiPayRest {

    private static Logger logger = LogManager.getLogger(ApiPayRest.class);

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/redisQueenPayTimeOut")
    public String redisQueenPayTimeOut(@RequestParam Map<String,Object> map){
        logger.info("redisQueenPayTimeOut-map==={}",map);

        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);

        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(String.valueOf(map.get("order_sn")),JSONObject.toJSONString(map));
//        redisService.set(String.valueOf(map.get("order_sn")),map);
        Integer multiple_hours = Integer.parseInt(map.get("multiple_hours").toString());
//        redisService.expire(String.valueOf(map.get("order_sn")),multiple_hours);
        boolean boo = redisTemplate.expire(String.valueOf(map.get("order_sn")),multiple_hours, TimeUnit.HOURS);
        logger.info("redisQueenPayTimeOut-redisTemplate.expire==="+boo);
        return "success";
    }
}
