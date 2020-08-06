package com.poweredbypace.pace.push.auth;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Universe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthInterceptor implements AtmosphereInterceptor {
	
	private static final long expirationTime = 1000 * 60 * 2;// two minutes
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	private final ConcurrentHashMap<String, String> authenticatedTokens = new ConcurrentHashMap<String, String>();
	
	public void configure(AtmosphereConfig arg0) {
		
	}
	
	private void cleanOldAuthTokens()
	{
		logger.debug("cleanOldAuthTokens");
		for(String uuid:authenticatedTokens.keySet())
		{
			//TODO: use @Inject to obtain a ref do th resourceFactory
			AtmosphereResource res = Universe.resourceFactory().find(uuid);
			if (res==null)
			{
				authenticatedTokens.remove(uuid);
				logger.debug("auth token removed, uuid="+uuid);
			}
		}
		logger.debug("Num auth tokens="+authenticatedTokens.size());
		long heapSize = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		long heapMaxSize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long heapFreeSize = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		logger.info("Memory: total:" + heapSize + " MB, max:" + heapMaxSize + " MB, free:" + heapFreeSize + " MB");
	}

	public Action inspect(AtmosphereResource res) {
		
		cleanOldAuthTokens();

		String uuid = (String)res.getRequest().getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID);
		AtmosphereResource originalResource = Universe.resourceFactory().find(uuid);
		
		if (originalResource==null)
			originalResource = res;
		
		String auth = res.getRequest().getHeader("X-Pace-Auth");
		logger.debug("Authorization:"+auth);
		
		logger.debug("inspect, uuid="+uuid);
		
		if (!authenticatedTokens.containsKey(uuid))
		{
			logger.debug("Auth Token not found");
			
			if (!isAuthenticated(auth))
			{
				logger.debug("NOT AUTHENTICATED");
				try {
					res.close();
				} catch (IOException e) {
					logger.error("Error", e);
				}
				return Action.CANCELLED;
			}
			authenticatedTokens.put(uuid, auth);
		} else {
			
			logger.debug("Auth Token found");
			
			if (!authenticatedTokens.get(uuid).equals(auth))
			{
				logger.debug("NOT AUTHENTICATED");
				try {
					res.close();
				} catch (IOException e) {
					logger.error("Error", e);
				}
				return Action.CANCELLED;
			}
		}
		
		logger.debug("AUTHENTICATED");
		
		return Action.CONTINUE;
	}

	public void postInspect(AtmosphereResource arg0) {
		
	}
	
	private boolean isAuthenticated(String token) {
		final String secret = "bRasw2d2as95gunu24E5raqeThuwuJuQudrata8a";
		
		String output = "";
		
		try {
			String[] tokens = token.split("x");
		
			long currentTime = new Date().getTime();
			long timestamp = Long.parseLong(tokens[0], 16);
			logger.debug(String.format("Timestamp: %d", timestamp));
			
			if (Math.abs(currentTime - timestamp) > expirationTime )
			{
				logger.debug("Auth token expired");
				return false;
			}
			
			String text = String.format("PACE %d %s", timestamp, secret); 
		
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        digest.update(text.getBytes("UTF-8"));
	        
	        byte[] hash = digest.digest();
	        BigInteger bigInt = new BigInteger(1, hash);
	        output = bigInt.toString(16);
	        
	        return output.equals(tokens[1]);
	        
	    } catch (Exception ex) {
	    	return false;
	    }
		
	}

	@Override
	public void destroy() {
		
	}
	
	
	

}
