package com.poweredbypace.pace.push;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.plugin.redis.RedisUtil;
import org.atmosphere.util.AbstractBroadcasterProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaceRedisBroadcaster extends AbstractBroadcasterProxy {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	public RedisConfig cfg;
	
	private RedisUtil redisUtil;

    public PaceRedisBroadcaster() {}

    public Broadcaster initialize(String id, AtmosphereConfig config) {
    		String redisUrl = cfg.getRedisUrl();
		URI uri = URI.create(redisUrl);
		log.info("Connecting to redis server "+redisUrl);
        return initialize(id, uri, config);
    }

    public Broadcaster initialize(String id, URI uri, AtmosphereConfig config) {
        String redisUrl = cfg.getRedisUrl();
        log.info("Connecting to redis server "+redisUrl);
		uri = URI.create(redisUrl);
        super.initialize(id, uri, config);
        this.redisUtil = new RedisUtil(uri, config, new RedisUtil.Callback() {
            @Override
            public String getID() {
                return PaceRedisBroadcaster.this.getID();
            }

            @Override
            public void broadcastReceivedMessage(String message) {
            		PaceRedisBroadcaster.this.broadcastReceivedMessage(message);
            }
        });
        setUp();
        return this;
    }

    public String getAuth() {
        return redisUtil.getAuth();
    }

    public void setAuth(String auth) {
        redisUtil.setAuth(auth);

    }

    public synchronized void setUp() {
        redisUtil.configure();
    }

    @Override
    public synchronized void setID(String id) {
        super.setID(id);
        setUp();
        reconfigure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        super.destroy();
        redisUtil.destroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incomingBroadcast() {
        redisUtil.incomingBroadcast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void outgoingBroadcast(Object message) {
        redisUtil.outgoingBroadcast(message);
    }
}
