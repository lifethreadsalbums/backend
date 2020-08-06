package com.poweredbypace.pace.interceptor;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

public class XSRFProtectionInterceptor extends HandlerInterceptorAdapter {
	
	private final Log log = LogFactory.getLog(XSRFProtectionInterceptor.class);
	
	private static final String XSRF_TOKEN = "XSRF-TOKEN";
	private static final String XSRF_HEADER = "X-XSRF-TOKEN";
	
	private boolean enabled = true;
	
	public void setEnabled(Boolean enabled) {
		this.enabled = BooleanUtils.isTrue(enabled);
	}

	private String getTokenForSession (HttpSession session) {
		 String token = null;
		 
		 Object mutex = WebUtils.getSessionMutex(session);
		 synchronized (mutex) {
		 	token = (String) session.getAttribute(XSRF_TOKEN);
		 	if (null==token) {
		 		token = UUID.randomUUID().toString();
		 		session.setAttribute(XSRF_TOKEN, token);
		 	}
		 }
		 return token;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	
		if (!enabled)
			return true;
		
		String sessionToken = getTokenForSession(request.getSession());
		response.addCookie(new Cookie(XSRF_TOKEN, sessionToken));
		
		
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// This is a POST request - need to check the CSRF token
			String requestToken = request.getHeader(XSRF_HEADER);
			
			if (sessionToken.equals(requestToken)) {
				return true;
			} else {
				log.error("Bad or missing XSRF token, session token=" + sessionToken + ", request token=" + requestToken);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing XSRF token");
				return false;
			}
	    }
		
		return true;
	}

}
