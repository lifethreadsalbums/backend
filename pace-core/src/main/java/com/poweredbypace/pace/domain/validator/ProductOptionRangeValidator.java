package com.poweredbypace.pace.domain.validator;

import java.util.Map;

import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionInteger;

public class ProductOptionRangeValidator extends ProductOptionValidator {

	@Override
	public boolean validate(ProductOption<?> object, Map<String, Object> map) {
		if(object.getClass().isAssignableFrom(ProductOptionInteger.class)) {
			Integer minValue = (Integer) map.get(ValidationConstants.MIN_VALUE);
			Integer maxValue = (Integer) map.get(ValidationConstants.MAX_VALUE);
			ProductOptionInteger option = (ProductOptionInteger) object;
			if(option.getValue() == null) return false;
			if(minValue != null && option.getValue() < minValue) return false;
			if(maxValue != null && option.getValue() > maxValue) return false;
			return true;
		}
		return false;
	}

}