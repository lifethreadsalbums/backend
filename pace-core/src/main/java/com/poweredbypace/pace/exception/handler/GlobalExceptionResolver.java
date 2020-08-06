package com.poweredbypace.pace.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.exception.ProductNotDesignedException;
import com.poweredbypace.pace.util.MessageUtil;
import com.poweredbypace.pace.util.SpringContextUtil;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver, Ordered {

	private final Log log = LogFactory.getLog(GlobalExceptionResolver.class);

	private Map<Class<? extends Throwable>, ExceptionResolver> handlers = 
			new HashMap<Class<? extends Throwable>, ExceptionResolver>();
	
	@PostConstruct
	public void init() {
		Map<String,Object> beans = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(ExceptionHandler.class);
		for(Object bean:beans.values()) {
			if (bean instanceof ExceptionResolver) {
				ExceptionResolver handler = (ExceptionResolver) bean;
				ExceptionHandler annotation = bean.getClass().getAnnotation(ExceptionHandler.class);
				for(Class<? extends Throwable> clazz:annotation.value()) {
					handlers.put(clazz, handler);
				}
			}
		}
	}
	
	 private ModelAndView resolveAsJson(HttpServletRequest request,
	            HttpServletResponse response, Object handler, Exception ex) {
	    	
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("error", MessageUtil.getMessage(ex));
		model.put("type", ex.getClass().getName());
		if (ex instanceof ProductNotDesignedException) {
			model.put("productId", ((ProductNotDesignedException) ex).getProductId());
			model.put("productType", ((ProductNotDesignedException) ex).getProductType());
		}
		if (ex instanceof AuthenticationException)
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		else
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
        return new ModelAndView(jsonView, model);
    }
	 
	private ModelAndView resolveAsHtml(HttpServletRequest request,
	            HttpServletResponse response, Object handler, Exception ex) {
	    	
        Map<String,Object> model = new HashMap<String, Object>();
		model.put("errorMessage", MessageUtil.getMessage(ex));
		model.put("exception", ex);
		return new ModelAndView("error", model);
	}
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		String principal = "";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth!=null && auth.getPrincipal()!=null) {
			if (auth.getPrincipal() instanceof User) { 
				User user = (User)auth.getPrincipal();
				principal = user.getEmail();
			} else {
				principal = auth.getPrincipal().toString();
			}
		}
		String path  = request.getRequestURI().substring(request.getContextPath().length());
		log.error("Error, path=" + path + ", user = " + principal, ex);
		
		ExceptionResolver exceptionHandler = handlers.get(ex.getClass());
		if (exceptionHandler!=null) {
			return exceptionHandler.resolveException(request, response, handler, ex);
		}
		
		if (path.startsWith("/api/"))
			return resolveAsJson(request, response, handler, ex);
		else 
			return resolveAsHtml(request, response, handler, ex);
		
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
