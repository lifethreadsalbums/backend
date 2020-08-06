package com.poweredbypace.pace.push.config;

import java.io.IOException;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.Universe;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.poweredbypace.pace.push.NotificationServer;

@Configuration
public class AppConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:config.properties"));
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(0);
	    return pc;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer productionPlaceHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(new PathMatchingResourcePatternResolver().getResources("file:/pace/conf/pushserver.properties"));
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(-1);
	    return pc;
	}
	
	@Bean
	public NotificationServer notificationCenterServer() {
		return new NotificationServer();
	}
	
	@Bean
	public AmazonSQS amazonSQS() {
		return new AmazonSQSClient(new BasicAWSCredentials(sqsAccessKey, sqsSecretKey));
	}
	
	@Bean
	public Broadcaster broadcaster() {
		return Universe.broadcasterFactory().lookup(atBroadcasterLookup);
	}
	
	@Bean
	public Nettosphere nettosphere() {
		Config.Builder builder = new Config.Builder();
		builder
			.port(atPort)
			.host(atHost)
			.initParam("org.atmosphere.cpr.AtmosphereInterceptor.disableDefaults", "false")
			.build();
		
		return new Nettosphere.Builder()
			.config(builder.build()).build();
	}
	
	@Value("${atmosphere.host}") private String atHost;
	@Value("${atmosphere.port}") private int atPort;
	@Value("${atmosphere.broadcasterLookup}") private String atBroadcasterLookup;
	@Value("${sqs.accessKey}") private String sqsAccessKey;
	@Value("${sqs.secretKey}") private String sqsSecretKey;
	@Value("${sqs.url}") private String sqsUrl;

}