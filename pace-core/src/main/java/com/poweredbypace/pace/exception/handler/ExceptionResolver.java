package com.poweredbypace.pace.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface ExceptionResolver {
	ModelAndView resolveException(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);
}
