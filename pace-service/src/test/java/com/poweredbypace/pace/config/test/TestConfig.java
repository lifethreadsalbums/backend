package com.poweredbypace.pace.config.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.impl.ImageServiceImpl;

@Configuration
public class TestConfig {

	@Bean
	public ImageService imageService() {
		return new ImageServiceImpl();
	}
	
}