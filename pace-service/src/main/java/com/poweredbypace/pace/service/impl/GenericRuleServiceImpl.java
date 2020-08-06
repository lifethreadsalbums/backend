package com.poweredbypace.pace.service.impl;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.exception.JsonException;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.repository.GenericRuleRepository;
import com.poweredbypace.pace.service.GenericRuleService;

@Service
public class GenericRuleServiceImpl implements GenericRuleService {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(getClass());
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private GenericRuleRepository genericRuleRepo;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Override
	public GenericRule findRule(Product product, String code) {
		List<GenericRule> sets = genericRuleRepo.findByCode(code);
		
		ProductContext productContext = new ProductContext(product);
		for(GenericRule rule:sets) {
			Boolean condition = expressionEvaluator.evaluate(productContext, 
					rule.getConditionExpression(), 
					Boolean.class);
			if (BooleanUtils.isTrue(condition))
				return rule;
		}
		return null;
	}
	
	@Override
	public GenericRule findRule(ExpressionContext ctx, String code) {
		List<GenericRule> sets = genericRuleRepo.findByCode(code);
		
		for(GenericRule rule:sets) {
			Boolean condition = expressionEvaluator.evaluate(ctx, 
					rule.getConditionExpression(), 
					Boolean.class);
			if (BooleanUtils.isTrue(condition))
				return rule;
		}
		return null;
	}
	
	@Override
	public GenericRule findRule(String code) {
		List<GenericRule> sets = genericRuleRepo.findByCode(code);
		if (sets.size()>0) return sets.get(0);
		return null;
	}
	
	@Override
	public <T> T getRuleValue(String code, Class<T> clazz) {
		GenericRule rule = findRule(code);
		if (rule==null) return null;
		try {
			T result = objectMapper.readValue(rule.getJsonData(), clazz);
			return result;
		} catch (Exception e) {
			throw new JsonException(e);
		} 
	}
	
	@Override
	public <T> T getRuleValue(Product product, String code, Class<T> clazz) {
		GenericRule rule = findRule(product, code);
		if (rule==null) return null;
		try {
			T result = objectMapper.readValue(rule.getJsonData(), clazz);
			return result;
		} catch (Exception e) {
			throw new JsonException(e);
		} 
	}
	
	@Override
	public <T> T getRuleValue(ExpressionContext ctx, String code, Class<T> clazz) {
		GenericRule rule = findRule(ctx, code);
		if (rule==null) return null;
		try {
			T result = objectMapper.readValue(rule.getJsonData(), clazz);
			return result;
		} catch (Exception e) {
			throw new JsonException(e);
		} 
	}
	
	@Override
	public <T> List<T> getRuleCollectionValue(ExpressionContext ctx, String code, Class<T> clazz) {
		GenericRule rule = findRule(ctx, code);
		if (rule==null) return null;
		try {
			List<T> result = objectMapper.readValue(rule.getJsonData(), 
					objectMapper.getTypeFactory().constructCollectionType(List.class, clazz) );
			return result;
		} catch (Exception e) {
			throw new JsonException(e);
		}
	}

	@Override
	public <T> List<T> getRuleCollectionValue(Product product, String code, Class<T> clazz) {
		GenericRule rule = findRule(product, code);
		if (rule==null) return null;
		try {
			List<T> result = objectMapper.readValue(rule.getJsonData(), 
					objectMapper.getTypeFactory().constructCollectionType(List.class, clazz) );
			return result;
		} catch (Exception e) {
			throw new JsonException(e);
		}
	}
}
