package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.expression.ExpressionContext;


public interface GenericRuleService{

	public GenericRule findRule(String code);
	public GenericRule findRule(Product product, String code);
	public GenericRule findRule(ExpressionContext ctx, String code);
	
	public <T> T getRuleValue(String code, Class<T> clazz);
	public <T> T getRuleValue(Product product, String code, Class<T> clazz);
	public <T> T getRuleValue(ExpressionContext ctx, String code, Class<T> clazz);
	public <T> List<T> getRuleCollectionValue(ExpressionContext product, String code, Class<T> clazz);
	public <T> List<T> getRuleCollectionValue(Product product, String code, Class<T> clazz);
	
}