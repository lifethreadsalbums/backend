package com.poweredbypace.pace.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	 
	private static final int SESSION_TIMEOUT =  2 * 60 * 60;
	
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        
    }
}