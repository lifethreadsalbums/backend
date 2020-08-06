package com.poweredbypace.pace.jobserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poweredbypace.pace.jobserver.mock.StorageServiceMock;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.service.impl.ImageServiceImpl;

@Configuration
public class TaskTestConfig {

	@Bean
	public ImageService imageService() {
		return new ImageServiceImpl();
	}
	
	
	@Bean
	public StorageService storageService() {
		return new StorageServiceMock();
	}
	
}