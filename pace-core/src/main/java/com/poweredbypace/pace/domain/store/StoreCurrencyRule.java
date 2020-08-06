package com.poweredbypace.pace.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name = "APP_STORE_CURRENCY_RULE")
public class StoreCurrencyRule extends BaseEntity {

	private static final long serialVersionUID = -6495246356572918791L;
	
	private String currency;
	private String conditionExpression;
	private Store store;
	
	public StoreCurrencyRule() { }

	@Column(name="CURRENCY")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "CONDITION_EXPRESSION", columnDefinition = "TEXT")
	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
