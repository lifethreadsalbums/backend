package com.poweredbypace.pace.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.manager.ProductManager;
import com.poweredbypace.pace.manager.ResourceManager;

@Component
public class SpringContextUtil implements ApplicationContextAware,BeanFactoryPostProcessor {

	private static ApplicationContext applicationContext;
	private static ConfigurableListableBeanFactory beanFactory;
	private static Env env;
	private static CurrencyManager currencyManager;
	private static ProductManager productManager;
	private static ExpressionEvaluator expressionEvaluator;
	private static ResourceManager resourceManager;
	
	public static ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}
	
	public static Env getEnv() {
		if (env==null) {
			try {
				env = getApplicationContext().getBean(Env.class);
			} catch(BeansException e) { }
		}
		return env;
	}
	
	public static ProductManager getProductManager() {
		if (productManager==null) {
			productManager = getApplicationContext().getBean(ProductManager.class);
		}
		return productManager;
	}
	
	public static CurrencyManager getCurrencyManager() {
		if (currencyManager==null) {
			currencyManager = getApplicationContext().getBean(CurrencyManager.class);
		}
		return currencyManager;
	}
	
	public static ExpressionEvaluator getExpressionEvaluator() {
		if (expressionEvaluator==null) {
			expressionEvaluator = getApplicationContext().getBean(ExpressionEvaluator.class);
		}
		return expressionEvaluator;
	}
	
	public static ResourceManager getResourceManager() {
		if (resourceManager==null) {
			resourceManager = getApplicationContext().getBean(ResourceManager.class);
		}
		return resourceManager;
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory bf) throws BeansException {
		beanFactory = bf;
	}
}
