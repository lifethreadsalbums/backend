package com.poweredbypace.pace.mail.impl;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.poweredbypace.pace.domain.mail.SmtpServer;
import com.poweredbypace.pace.repository.SmtpServerRepository;
import com.poweredbypace.pace.util.SpringContextUtil;

public class JavaMailSenderConfigurer {

	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private SmtpServerRepository smtpServerRepo;
	
	
	@PostConstruct
	public void postConstruct() {
		log.debug("post construct");
		
		ConfigurableListableBeanFactory beanFactory = SpringContextUtil.getBeanFactory();
		
		for(SmtpServer smtpServer:smtpServerRepo.findAll())
		{
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
					.rootBeanDefinition(AsyncJavaMailSenderImpl.class)
					.addPropertyValue("host", smtpServer.getHost())
					.addPropertyValue("port", smtpServer.getPort())
					.addPropertyValue("username", smtpServer.getUsername())
					.addPropertyValue("password", smtpServer.getPassword())
					.addPropertyValue("protocol", smtpServer.getProtocol());
			
			Properties props = smtpServer.getJavaMailProperties();
			if (props!=null)
				beanDefinitionBuilder.addPropertyValue("javaMailProperties", props);
		 
			BeanDefinitionRegistry factory = (BeanDefinitionRegistry) beanFactory;
			factory.registerBeanDefinition("JavaMailSender" + smtpServer.getId(), 
					beanDefinitionBuilder.getBeanDefinition());
		}
	}
	
	
}
