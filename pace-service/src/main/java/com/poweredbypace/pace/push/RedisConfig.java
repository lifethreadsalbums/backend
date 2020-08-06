package com.poweredbypace.pace.push;

import org.springframework.stereotype.Service;

@Service
public class RedisConfig {
	private String redisUrl;

	public String getRedisUrl() {
		return redisUrl;
	}

	public void setRedisUrl(String redisUrl) {
		this.redisUrl = redisUrl;
	}
	
}
