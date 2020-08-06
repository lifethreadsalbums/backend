package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "P_PRODUCT_OPTION_STRING")
public class ProductOptionString extends ProductOption<String> {

	private static final long serialVersionUID = 7374235254914614850L;

	private String stringValue;
	
	@Column(name = "STRING_VALUE", columnDefinition = "TEXT")
	private String getStringValue() {
		return stringValue;
	}
	
	private void setStringValue(String value) {
		this.stringValue = value;
	}

	@Transient
	public String getValue() {
		return getStringValue();
	}

	public void setValue(String value) {
		setStringValue(value);
		
	}
}
