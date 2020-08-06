package com.poweredbypace.pace.domain.validator;

import java.util.ArrayList;
import java.util.List;

import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.Validator;

public class ProductOptionValidationError {
	
	private ProductOption<?> productOption;
	private List<Validator> falseValidators = new ArrayList<Validator>();
	
	public ProductOption<?> getProductOption() {
		return productOption;
	}
	public void setProductOption(ProductOption<?> productOption) {
		this.productOption = productOption;
	}
	public List<Validator> getFalseValidators() {
		return falseValidators;
	}
	public void setFalseValidators(List<Validator> falseValidators) {
		this.falseValidators = falseValidators;
	}
}
