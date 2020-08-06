package com.poweredbypace.pace.util;

import org.apache.commons.lang.StringUtils;

public class NameUtil {
	
	public static String getFullName(String firstName, String lastName, String companyName) {
		StringBuffer buffer = new StringBuffer();
		boolean hasCompanyName = !StringUtils.isEmpty(companyName);
		if(hasCompanyName) {
			buffer.append(companyName);
			buffer.append(" (");
		}
		buffer.append(firstName);
		buffer.append(" ");
		buffer.append(lastName);
		if(hasCompanyName) {
			buffer.append(")");
		}
		return buffer.toString();
	}
}
