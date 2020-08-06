package com.poweredbypace.pace.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentGatewayConfiguration {

	private String gatewayUrl;
	private String httpMethod = "POST";
	
	private Map<String,String> formFields = new HashMap<String, String>();
	
	public String getGatewayUrl() {
		return gatewayUrl;
	}
	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
	public Map<String, String> getFormFields() {
		return formFields;
	}
	public void setFormFields(Map<String, String> formFields) {
		this.formFields = formFields;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
}
