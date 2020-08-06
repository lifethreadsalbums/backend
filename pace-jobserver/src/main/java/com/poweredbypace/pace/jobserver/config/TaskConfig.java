package com.poweredbypace.pace.jobserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={
		"com.poweredbypace.pace.job.task",
		"com.poweredbypace.pace.event"
})
public class TaskConfig {


}