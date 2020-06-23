package com.cff.springbootwork.redis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cff.springbootwork.redis.RedisService;

@RestController
@RequestMapping("/redis")
public class RedisRest {
	@Autowired
	RedisService redisService;

	@RequestMapping(value = "/set", method = { RequestMethod.GET })
	public String set(@RequestParam String value) {
		redisService.set("testredis", value);

		return "0000";
	}

	@RequestMapping(value = "/get", method = { RequestMethod.GET })
	public String get() {
		String value = (String) redisService.get("testredis");

		return value;
	}
}
