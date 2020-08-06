package com.poweredbypace.pace.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.poweredbypace.pace.util.MessageUtil;

public class Http403ForbiddenEntryPoint implements AuthenticationEntryPoint {
    private static final Log logger = LogFactory.getLog(Http403ForbiddenEntryPoint.class);

    /**
     * Always returns a 403 error code to the client.
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException,
            ServletException {
    	
    	MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("error", MessageUtil.getMessage(ex));
		model.put("type", ex.getClass().getName());
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		try {
			jsonView.render(model, request, response);
		} catch (Exception e) {
			logger.error(e);
		}
    }


}
