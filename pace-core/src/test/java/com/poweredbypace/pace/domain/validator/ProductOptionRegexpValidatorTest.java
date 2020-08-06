package com.poweredbypace.pace.domain.validator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import com.poweredbypace.pace.domain.ProductOptionString;

public class ProductOptionRegexpValidatorTest {

	private ProductOptionRegexpValidator validator = null;
	
	@Before
	public void setUp() {
		this.validator = new ProductOptionRegexpValidator();
	}
	
	@Test
	public void successEmailMatching() {
		ProductOptionString productOptionString = new ProductOptionString();
		productOptionString.setValue("admin@example.com");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ValidationConstants.EXPRESSION, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");

		boolean result = validator.validate(productOptionString, params);
		Assert.assertTrue(result);
	}
	
	@Test
	public void failureEmailMatching() {
		ProductOptionString productOptionString = new ProductOptionString();
		productOptionString.setValue("adminATexample.com");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ValidationConstants.EXPRESSION, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");

		boolean result = validator.validate(productOptionString, params);
		Assert.assertFalse(result);
	}
}
