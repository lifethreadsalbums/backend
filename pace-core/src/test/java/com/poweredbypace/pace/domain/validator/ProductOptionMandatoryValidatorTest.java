package com.poweredbypace.pace.domain.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.poweredbypace.pace.domain.ProductOptionInteger;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;

public class ProductOptionMandatoryValidatorTest {

	private ProductOptionMandatoryValidator validator = null;
	
	@Before
	public void setUp() {
		this.validator = new ProductOptionMandatoryValidator();
	}
	
	@Test
	public void successIntTest() {
		ProductOptionInteger productOptionInteger = new ProductOptionInteger();
		productOptionInteger.setValue(1);
		
		boolean result = validator.validate(productOptionInteger);
		Assert.assertTrue(result);
	}

	@Test
	public void failIntTest() {
		ProductOptionInteger productOptionInteger = new ProductOptionInteger();
		
		boolean result = validator.validate(productOptionInteger);
		Assert.assertFalse(result);
	}
	
	@Test
	public void successOptionValueTest() {
		ProductOptionValue productOptionValue = new ProductOptionValue();
		productOptionValue.setValue(new PrototypeProductOptionValue());
		
		boolean result = validator.validate(productOptionValue);
		Assert.assertTrue(result);
	}

	@Test
	public void failOptionValueTest() {
		ProductOptionValue productOptionValue = new ProductOptionValue();
		
		boolean result = validator.validate(productOptionValue);
		Assert.assertFalse(result);
	}
}
