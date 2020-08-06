package com.poweredbypace.pace.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.spring.SpringWebObjectFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.poweredbypace.pace.security.SessionListener;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{

	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return new Class<?>[] { AppConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class<?>[] { WebMvcConfig.class };
	}

	@Override
	protected String[] getServletMappings()
	{
		return new String[] { "/" };
	}
	
	@Override
    protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setEncoding("UTF-8");
	    characterEncodingFilter.setForceEncoding(true);
	    
	    return new Filter[] { characterEncodingFilter };
    }

//	@Override
//	public void onStartup(ServletContext servletContext)
//			throws ServletException {
//		super.onStartup(servletContext);
//		servletContext.addListener(new SessionListener());
//	} 
	
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		servletContext.addListener(new SessionListener());
		
		ServletRegistration.Dynamic atmosphereServlet = 
				servletContext.addServlet("atmosphereServlet", new AtmosphereServlet());
	   // servletContext.addListener(new org.atmosphere.cpr.SessionSupport());
	    atmosphereServlet.addMapping("/websocket/*");
	    atmosphereServlet.setLoadOnStartup(0);
	    atmosphereServlet.setAsyncSupported(true);
	    atmosphereServlet.setInitParameter(ApplicationConfig.AUTODETECT_BROADCASTER, "true");
	    atmosphereServlet.setInitParameter(ApplicationConfig.OBJECT_FACTORY, SpringWebObjectFactory.class.getName());
	    
	} 
}
