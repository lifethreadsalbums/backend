package com.poweredbypace.pace.domain.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.Validator;

public class ProductValidator {

	private final Log log = LogFactory.getLog(ProductValidator.class);
	
	public List<ProductOptionValidationError> validate(Product product) {
		List<ProductOptionValidationError> entries = new ArrayList<ProductOptionValidationError>();
		for(ProductOption<?> option : product.getProductOptions()) {
			for(Validator validator : option.getPrototypeProductOption().getEffectiveValidators()) {
				try {
					if(!validator.validate(option)) {
						ProductOptionValidationError entry = getEntry(option, entries);
						entry.getFalseValidators().add(validator);
					}
				} catch (InstantiationException e) {
					log.error(e.getStackTrace(), e);
				} catch (IllegalAccessException e) {
					log.error(e.getStackTrace(), e);
				}
			}
		}
		return entries;
	}

	private ProductOptionValidationError getEntry(ProductOption<?> option, List<ProductOptionValidationError> entries) {
		for(ProductOptionValidationError entry : entries) {
			if(option.equals(entry.getProductOption())) return entry;
		}
		ProductOptionValidationError entry = new ProductOptionValidationError();
		entry.setProductOption(option);
		entries.add(entry);
		return entry;
	}
}
