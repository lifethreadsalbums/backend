package com.poweredbypace.pace.domain.validator;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.poweredbypace.pace.domain.ProductOptionInteger;

public class ProductOptionRangeValidatorTest {

	
	@Before
	public void setUp() {
	}

	@Test
	public void minMaxIntSuccessTest() {
		ProductOptionInteger mockIntOption = new ProductOptionInteger();
		mockIntOption.setValue(10);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ValidationConstants.MIN_VALUE, 5);
		params.put(ValidationConstants.MAX_VALUE, 15);
		
		ProductOptionRangeValidator validator = new ProductOptionRangeValidator();
		boolean result = validator.validate(mockIntOption, params);
		Assert.assertTrue(result);
	}

	@Test
	public void minIntFailTest() {
		ProductOptionInteger mockIntOption = new ProductOptionInteger();
		mockIntOption.setValue(10);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ValidationConstants.MIN_VALUE, 11);
		
		ProductOptionRangeValidator validator = new ProductOptionRangeValidator();
		boolean result = validator.validate(mockIntOption, params);
		Assert.assertFalse(result);
	}

	@Test
	public void maxIntFailTest() {
		ProductOptionInteger mockIntOption = new ProductOptionInteger();
		mockIntOption.setValue(10);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ValidationConstants.MAX_VALUE, 9);
		
		ProductOptionRangeValidator validator = new ProductOptionRangeValidator();
		boolean result = validator.validate(mockIntOption, params);
		Assert.assertFalse(result);
	}
}
