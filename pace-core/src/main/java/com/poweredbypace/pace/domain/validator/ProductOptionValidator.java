package com.poweredbypace.pace.domain.validator;

import java.util.HashMap;
import java.util.Map;

import com.poweredbypace.pace.domain.ProductOption;

public abstract class ProductOptionValidator {

	public boolean validate(ProductOption<?> object) {
		return validate(object, new HashMap<String, Object>());
	}

	public abstract boolean validate(ProductOption<?> object, Map<String, Object> map);
}
