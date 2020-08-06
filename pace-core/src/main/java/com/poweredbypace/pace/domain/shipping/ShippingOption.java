package com.poweredbypace.pace.domain.shipping;

public class ShippingOption {

	private String name;
	private String code;
	private String providerId;
	private Boolean christmasShipping = null;
	private Boolean freeShipping = false;

	public ShippingOption() { }
	
	public ShippingOption(String providerId, String name, String code) {
		this.providerId = providerId;
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String shipId) {
		this.code = shipId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Boolean getChristmasShipping() {
		return christmasShipping;
	}

	public void setChristmasShipping(Boolean christmasShipping) {
		this.christmasShipping = christmasShipping;
	}

	public Boolean getFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}
	
}
