package com.poweredbypace.pace.shipping;

public class ShippingResponse {

	public static enum ResponseEnum {
		OK,
		MAXIMUM_WEIGHT_EXCEEDED,
		SERVICE_UNAVAILABLE_BETWEEN_SELECTED_LOCATIONS,
		SERVICE_UNACCESIBLEs,
		CREDENTIALS_PROBLEM,
		TIMEOUT,
		UNCATEGORIZED
	}
	
	private ResponseEnum responseEnum;
	private String message;
	
	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}
	public void setResponseEnum(ResponseEnum responseEnum) {
		this.responseEnum = responseEnum;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}