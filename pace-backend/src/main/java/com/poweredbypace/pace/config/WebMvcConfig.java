package com.poweredbypace.pace.config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.poweredbypace.pace.interceptor.BasicDataInterceptor;
import com.poweredbypace.pace.json.JsonEnumModule;
import com.poweredbypace.pace.mail.EmailTemplateLoader;
import com.poweredbypace.pace.view.FreeMarkerViewWithPdfSupport;

@Configuration
@ComponentScan(basePackages = { "com.poweredbypace.pace.controller" },
	useDefaultFilters = false, includeFilters = @Filter({ Controller.class }))
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
		b.handlerInstantiator(new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory()));
		
		Hibernate4Module hibernateMod = new Hibernate4Module();
		hibernateMod.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING , true);
		b.modules(hibernateMod, new JsonEnumModule()); //, new DomainEntityModule());
		b.dateFormat(new ISO8601DateFormat());
		b.failOnEmptyBeans(false);
		//b.defaultViewInclusion(true);
		
		converters.add(new MappingJackson2HttpMessageConverter(b.build()));
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}

	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/crossdomain.xml").addResourceLocations("/");
		registry.addResourceHandler("/health-check.html").addResourceLocations("/");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/styles/**").addResourceLocations("/WEB-INF/app/styles/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("/WEB-INF/app/scripts/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/app/images/").setCachePeriod(604800);
        registry.addResourceHandler("/i18n/**").addResourceLocations("/WEB-INF/app/i18n/");
        registry.addResourceHandler("/views/**").addResourceLocations("/WEB-INF/app/views/");
        registry.addResourceHandler("/bower_components/**").addResourceLocations("/WEB-INF/app/bower_components/");
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(basicDataInterceptor());
	    registry.addInterceptor(localeChangeInterceptor());
	}

	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("l");
		return interceptor;
	}
	
	@Bean
	public BasicDataInterceptor basicDataInterceptor() {
		return new BasicDataInterceptor();
	}
	
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(1073741824); //1gb
		return resolver;
	}
	
	@Bean
	public UrlBasedViewResolver urlBasedViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(2);
		resolver.setViewClass(JstlView.class);
		return resolver;
	}
	
	@Bean
	public EmailTemplateLoader emailTemplateLoader() {
		return new EmailTemplateLoader();
	}
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfig() {
		FreeMarkerConfigurer config = new FreeMarkerConfigurer();
		//config.setTemplateLoaderPath("/WEB-INF/app/");
		config.setTemplateLoaderPaths("/WEB-INF/app/", "/WEB-INF/views/");
		config.setPreTemplateLoaders(emailTemplateLoader());
		return config;
	}
	
	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setOrder(1);
		resolver.setCache(true);
		resolver.setPrefix("");
		resolver.setSuffix(".html");
		resolver.setExposeSpringMacroHelpers(true);
		resolver.setViewClass(FreeMarkerViewWithPdfSupport.class);
		return resolver;
	}
	
	@Bean
	public UrlFilenameViewController staticViewController() {
		return new UrlFilenameViewController();
	}
	
	@Bean
	public SimpleUrlHandlerMapping urlMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		Map<String, Object> urlMap = new HashMap<String, Object>();
		urlMap.put("/*.html", staticViewController());
		mapping.setUrlMap(urlMap);
		return mapping;
	}
	
	
}