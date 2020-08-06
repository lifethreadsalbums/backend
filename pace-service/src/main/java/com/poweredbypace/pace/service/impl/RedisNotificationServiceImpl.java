package com.poweredbypace.pace.service.impl;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationTarget;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.util.JsonUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

@Service
public class RedisNotificationServiceImpl implements NotificationBroadcaster {

	protected final ObjectMapper mapper = new ObjectMapper();
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Value("${redis.url}") 
	private String redisUrl;
		
	private Jedis jedisPublisher;
	
	public RedisNotificationServiceImpl() { }

	private void initRedis() {
		URI uri = URI.create(redisUrl);
		
		jedisPublisher = new Jedis(uri.getHost(), uri.getPort());
        try {
            jedisPublisher.connect();
        } catch (JedisException e) {
            logger.error("failed to connect publisher", e);
            disconnectPublisher();
        }
        
	}

	private void disconnectPublisher() {
	    if (jedisPublisher == null) return;
	
	    synchronized (jedisPublisher) {
	        try {
	            jedisPublisher.disconnect();
	        } catch (JedisException e) {
	            logger.error("failed to disconnect publisher", e);
	        }
	    }
	}
	
	@Override
	@Async
	public void broadcast(Notification n) {
		sendNotification(n);
	}
	
	private void sendNotification(Notification n) {
		if (jedisPublisher==null) {
			initRedis();
		}
		
		String message = null;
		try {
			message = mapper.writeValueAsString(n);
		} catch (Exception e) {
			logger.error("json exception", e);
			return;
		} 
		
        synchronized (jedisPublisher) {
            try {
                jedisPublisher.publish("/websocket/studio", message);
            } catch (JedisException e) {
                logger.warn("outgoingBroadcast exception", e);
            }
        }
	}
	
	public Notification createNotification(NotificationType type, Object o, NotificationTarget target) {
		Notification n = new Notification();
		n.setType(type);
		n.setSenderId(getSessionId());
		n.setTarget(target);
		n.setBody(JsonUtil.serialize(o));
		return n;
	}
	
	private String getSessionId() {
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) 
					RequestContextHolder.currentRequestAttributes();
			
			if (attr!=null)
				return attr.getSessionId();
		} catch(Throwable t) {}
		
		return null;
	}

}
