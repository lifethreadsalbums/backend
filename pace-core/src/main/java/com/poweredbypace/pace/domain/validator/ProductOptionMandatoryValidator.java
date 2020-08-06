package com.poweredbypace.pace.domain.validator;

import java.util.Map;

import com.poweredbypace.pace.domain.ProductOption;

public class ProductOptionMandatoryValidator extends ProductOptionValidator {

	@Override
	public boolean validate(ProductOption<?> object, Map<String, Object> map) {
		if(object.getValue() != null) {
			return true;
		}
		return false;
	}
	
}