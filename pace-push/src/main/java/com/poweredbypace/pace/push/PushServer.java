package com.poweredbypace.pace.push;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.poweredbypace.pace.push.config.AppConfig;

public class PushServer {
	
	 public static void main(String[] args) throws IOException {
		//workaround for this issue: https://github.com/aws/aws-sdk-java/issues/123
	    System.setProperty("entityExpansionLimit", "0");
	    System.setProperty("jdk.xml.entityExpansionLimit", "0");
	    	
	    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
	    NotificationServer server = ctx.getBean(NotificationServer.class);
	    server.start();
		ctx.close();
	}

}
