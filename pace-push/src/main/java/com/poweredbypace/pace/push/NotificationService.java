package com.poweredbypace.pace.push;

import org.atmosphere.cache.DefaultBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.CorsInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;
import org.atmosphere.interceptor.IdleResourceInterceptor;
import org.atmosphere.interceptor.JavaScriptProtocol;
import org.atmosphere.interceptor.OnDisconnectInterceptor;
import org.atmosphere.interceptor.SSEAtmosphereInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;


@ManagedService( path = "/studio",
	interceptors = {
		
		CorsInterceptor.class,
		//AuthInterceptor.class,
		
		OnDisconnectInterceptor.class,
		JavaScriptProtocol.class,
		//JSONPAtmosphereInterceptor.class,
		SSEAtmosphereInterceptor.class,
		//AndroidAtmosphereInterceptor.class,
		//PaddingAtmosphereInterceptor.class,
		//DefaultHeadersInterceptor.class, 
		IdleResourceInterceptor.class,
		AtmosphereResourceLifecycleInterceptor.class,
	    TrackMessageSizeInterceptor.class,
	    HeartbeatInterceptor.class,
	    SuspendTrackerInterceptor.class
	    //,AnnotationServiceInterceptor.class
	    
	},
	broadcasterCache=DefaultBroadcasterCache.class
//	atmosphereConfig={
//		"org.atmosphere.cpr.broadcaster.shareableThreadPool=true",
//		"org.atmosphere.cpr.broadcaster.maxAsyncWriteThreads=10",
//		"org.atmosphere.cpr.broadcaster.maxProcessingThreads=10"
//		}
)
public class NotificationService {
	
    //private final ObjectMapper mapper = new ObjectMapper();
    
    //private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
//	@Message
//	public String onMessage(String message) throws IOException {
//		//logger.trace("onMessage:" + message);
//		return message;
//		//return  mapper.writeValueAsString(mapper.readValue(message, Notification.class));
//	}
	
	
}
