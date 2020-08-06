package com.poweredbypace.pace.push;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cache.DefaultBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Get;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;
import org.atmosphere.interceptor.IdleResourceInterceptor;
import org.atmosphere.interceptor.JavaScriptProtocol;
import org.atmosphere.interceptor.OnDisconnectInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;

@ManagedService(path = "/websocket/studio", 
	interceptors = {
		OnDisconnectInterceptor.class, 
		JavaScriptProtocol.class,
		IdleResourceInterceptor.class, 
		AtmosphereResourceLifecycleInterceptor.class, 
		TrackMessageSizeInterceptor.class,
		HeartbeatInterceptor.class, 
		SuspendTrackerInterceptor.class
	}, 
	broadcasterCache = DefaultBroadcasterCache.class,
	broadcaster = PaceRedisBroadcaster.class
)
public class PushService {

	private final Log log = LogFactory.getLog(getClass());

	@Get
	public void init(AtmosphereResource resource) {
		// Set the character encoding as atmospheres default is not unicode.
		// resource.getResponse().setCharacterEncoding(Charsets.UTF_8);
	}

	@Ready
	public String onReady(final AtmosphereResource resource) {
		log.info("Browser " + resource.uuid() + " connected.");
		return "";
	}

	@Message
	public String onMessage(String message) throws IOException {
		//log.trace("onMessage:" + message);
		return message;
	}

	@Disconnect
	public void onDisconnect(AtmosphereResourceEvent event) {
		if (event.isCancelled()) {
			log.info("Browser " + event.getResource().uuid() + " unexpectedly disconnected");
		} else if (event.isClosedByClient()) {
			log.info("Browser " + event.getResource().uuid() + " closed the connection");
		}
	}

}
