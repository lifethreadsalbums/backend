package com.poweredbypace.pace.jobserver;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.poweredbypace.pace.jobserver.config.AppConfig;
import com.poweredbypace.pace.jobserver.config.TaskConfig;

public class JobServer {
	
	public static void main(String[] args) {
		new JobServer();
	}
	
	public JobServer() {
		//workaround for this issue: https://github.com/aws/aws-sdk-java/issues/123
    	System.setProperty("entityExpansionLimit", "0");
    	System.setProperty("jdk.xml.entityExpansionLimit", "0");
    	
    	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class, TaskConfig.class);
	    QueueProcessor qp = ctx.getBean(QueueProcessor.class);
	    qp.processQueue();
	    ctx.close();
	}

}
