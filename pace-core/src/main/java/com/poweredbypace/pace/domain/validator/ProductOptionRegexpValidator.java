package com.poweredbypace.pace.domain.validator;

import java.util.Map;

import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionString;

public class ProductOptionRegexpValidator extends ProductOptionValidator {

	@Override
	public boolean validate(ProductOption<?> object, Map<String, Object> map) {
		ProductOptionString option = (ProductOptionString) object;
		String pattern = getExpression(map);
		return option.getValue() != null && option.getValue().toUpperCase().matches(pattern);
	}

	private String getExpression(Map<String, Object> map) {
		return (String) map.get(ValidationConstants.EXPRESSION);
	}
}
