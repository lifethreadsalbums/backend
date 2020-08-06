package com.poweredbypace.pace.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;


@ExceptionHandler({NullPointerException.class})
//@Component
public class SampleExceptionHandler implements ExceptionResolver {

	private final Log log = LogFactory.getLog(SampleExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		log.error("Null pointer exception handler");
		
		return null;
	}

}
