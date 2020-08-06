package com.poweredbypace.pace.domain.validator;

import java.util.List;

import org.apache.commons.logging.Log;

import com.poweredbypace.pace.domain.Validator;

public class ValidationHandler {
	
	public static void handleAsException(List<ProductOptionValidationError> entries) throws ValidationException {
		if(entries != null && entries.size() > 0) {
			StringBuffer buf = getMessage(entries);
			throw new ValidationException(buf.toString());
		}
	}

	public static void handleAsLog(List<ProductOptionValidationError> entries, Log log) {
		for(String line : getMessageLines(entries)) {
			log.debug(line);
		}
	}

	private static StringBuffer getMessage(
			List<ProductOptionValidationError> entries) {
		StringBuffer buf = new StringBuffer();
		boolean firstEntry = true;
		for(ProductOptionValidationError entry : entries) {
			if(!firstEntry) {
				buf.append("\r\n");
			}
			firstEntry = false;
			buf.append(entry.getProductOption().getDisplayName());
			buf.append(": ");
			boolean firstValidator = true;
			for(Validator validator : entry.getFalseValidators()) {
				if(!firstValidator) {
					buf.append(", ");
				} 
				firstValidator = false;
				buf.append(validator.gettValidator().getDisplayName());
			}
		}
		return buf;
	}

	private static String[] getMessageLines(
			List<ProductOptionValidationError> entries) {
		return getMessage(entries).toString().split("\n");
	}
}
