package com.poweredbypace.pace.util;

public class AddressUtil {
	
	public static String getCleanZipcode(String zipcode) {
		return zipcode.replaceAll("-", "");
	}
}
