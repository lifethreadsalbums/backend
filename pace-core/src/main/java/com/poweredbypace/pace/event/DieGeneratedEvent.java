package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class DieGeneratedEvent extends ProductEvent {
	
	private String optionCode;
	
	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public DieGeneratedEvent() { }

	public DieGeneratedEvent(Product product, String optionCode) {
		super(product);
		this.optionCode = optionCode;
	}
	
}
