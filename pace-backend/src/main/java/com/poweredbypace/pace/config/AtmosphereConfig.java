package com.poweredbypace.pace.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.BroadcasterLifeCyclePolicy.ATMOSPHERE_RESOURCE_POLICY;
import org.atmosphere.spring.bean.AtmosphereSpringContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poweredbypace.pace.push.PushService;
import com.poweredbypace.pace.push.RedisConfig;

@Configuration 
public class AtmosphereConfig {
	
	@Value("${redis.url}") private String redisUrl;

	@Bean
	public AtmosphereFramework atmosphereFramework() throws ServletException, InstantiationException, IllegalAccessException {
	   AtmosphereFramework atmosphereFramework = new AtmosphereFramework(false, false);
	   // atmosphereFramework.setBroadcasterCacheClassName(UUIDBroadcasterCache.class.getName());
	   return atmosphereFramework;
	}
	
	//private List<AtmosphereInterceptor> interceptors() {
	//   List<AtmosphereInterceptor> atmosphereInterceptors = new ArrayList<AtmosphereInterceptor>();
	//   // atmosphereInterceptors.add(new TrackMessageSizeInterceptor());
	//   return atmosphereInterceptors;
	//}
	
	@Bean
	public BroadcasterFactory broadcasterFactory() throws ServletException, InstantiationException, IllegalAccessException {
	   return atmosphereFramework().getAtmosphereConfig().getBroadcasterFactory();
	}
	
	@Bean
	public RedisConfig cfg() {
		RedisConfig cfg = new RedisConfig();
		cfg.setRedisUrl(redisUrl);
		return cfg;
	}
	
	@Bean
	public AtmosphereSpringContext atmosphereSpringContext() {
	   AtmosphereSpringContext atmosphereSpringContext = new AtmosphereSpringContext();
	   Map<String, String> map = new HashMap<String, String>();
	   map.put(ApplicationConfig.ANNOTATION_PACKAGE, PushService.class.getPackage().getName());
	   
	   map.put("org.atmosphere.cpr.broadcasterClass", org.atmosphere.cpr.DefaultBroadcaster.class.getName());
	   map.put(AtmosphereInterceptor.class.getName(), TrackMessageSizeInterceptor.class.getName());
	   //map.put(AnnotationProcessor.class.getName(), VoidAnnotationProcessor.class.getName());
	   map.put( ApplicationConfig.WEBSOCKET_SUPPORT_SERVLET3, "true");
	   map.put("org.atmosphere.plugin.redis.RedisBroadcaster.server", redisUrl);
	   map.put("org.atmosphere.cpr.broadcasterLifeCyclePolicy", ATMOSPHERE_RESOURCE_POLICY.IDLE_DESTROY.toString());
	   atmosphereSpringContext.setConfig(map);
	   return atmosphereSpringContext;
	}
}
