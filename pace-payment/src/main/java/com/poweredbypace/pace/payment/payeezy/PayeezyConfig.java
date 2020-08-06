package com.poweredbypace.pace.payment.payeezy;

import java.util.HashMap;
import java.util.Map;

public class PayeezyConfig {
	
	private Map<String, String> paymentPageIds = new HashMap<String, String>();
	private Map<String, String> transactionKeys = new HashMap<String, String>();
	private Map<String, SoftDescriptorInfo> softDescriptors = new HashMap<String, SoftDescriptorInfo>();
	private String gatewayUrl;
	
	public Map<String, String> getPaymentPageIds() {
		return paymentPageIds;
	}

	public void setPaymentPageIds(Map<String, String> paymentPageIds) {
		this.paymentPageIds = paymentPageIds;
	}

	public Map<String, String> getTransactionKeys() {
		return transactionKeys;
	}

	public void setTransactionKeys(Map<String, String> transactionKeys) {
		this.transactionKeys = transactionKeys;
	}
	
	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public Map<String, SoftDescriptorInfo> getSoftDescriptors() {
		return softDescriptors;
	}

	public void setSoftDescriptors(Map<String, SoftDescriptorInfo> softDescriptors) {
		this.softDescriptors = softDescriptors;
	}

}
