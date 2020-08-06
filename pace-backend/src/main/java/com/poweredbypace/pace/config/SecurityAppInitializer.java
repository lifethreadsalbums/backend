package com.poweredbypace.pace.config;

import javax.servlet.ServletContext;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityAppInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        insertFilters(servletContext, 
        	//new HiddenHttpMethodFilter(), 
        	new OpenEntityManagerInViewFilter());
    }
	
	@Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}
