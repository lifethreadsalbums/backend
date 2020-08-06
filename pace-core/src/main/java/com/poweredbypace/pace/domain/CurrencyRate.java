package com.poweredbypace.pace.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="APP_CURRENCY_RATE")
public class CurrencyRate extends BaseEntity {

	private static final long serialVersionUID = -3282728884003663313L;
	
	private String currencyFrom;
	private String currencyTo;
	private BigDecimal rate;
	private BigDecimal factor;
	
	public CurrencyRate() {}
	
	public CurrencyRate(String currencyFrom, String currencyTo, BigDecimal rate) {
		super();
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.rate = rate;
	}
	
	@Column(name="CURRENCY_FROM")
	public String getCurrencyFrom() {
		return currencyFrom;	
	}
	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}
	
	@Column(name="CURRENCY_TO")
	public String getCurrencyTo() {
		return currencyTo;
	}
	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}
	
	@Column(name="RATE")
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Column(name="FACTOR")
	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	
	@Transient
	public BigDecimal getStoreRate() {
		if(getFactor() != null) {
			return getRate().multiply(getFactor());
		}
		return getRate();
	}
}
