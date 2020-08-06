package com.poweredbypace.pace.dto;

import java.util.Currency;

public class CurrencyDto {
	
	private Currency currency;
	
	public String getCurrencyCode() {
		return currency.getCurrencyCode();
	}
	
	public CurrencyDto(Currency currency) {
		this.currency = currency;
	}
}
