package com.poweredbypace.pace.domain;

import java.util.Currency;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrencyInfo {
	
	private Currency currency;
	
	public String getCurrencyCode() {
		return currency.getCurrencyCode();
	}
	
	@JsonIgnore
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public CurrencyInfo(Currency currency) {
		this.currency = currency;
	}
	
	public CurrencyInfo(String countryCode) {
		this.currency = Currency.getInstance(new Locale("", countryCode));
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getCurrency());
		return b.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if (o == null)
		    return false;
		
		if (this.getClass() != o.getClass()) {
	        return false;
	    }
		  
		CurrencyInfo that = (CurrencyInfo)o;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getCurrency(), that.getCurrency());
		return b.isEquals();
	}
	
	
}
