package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_PRODUCT_OPTION_INT")
public class ProductOptionInteger extends ProductOption<Integer> {

	private static final long serialVersionUID = -4075985042175162396L;

	private Integer value;
	
	@Column(name = "INTEGER_VALUE")
	private Integer getIntValue() {
		return value;
	}

	private void setIntValue(Integer value) {
		this.value = value;
	}

	@Transient
	public Integer getValue() {
		return getIntValue();
	}

	public void setValue(Integer value) {
		setIntValue(value);
	}
	
	
}
