package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_PRODUCT_OPTION_DOUBLE")
public class ProductOptionDouble extends ProductOption<Double> {

	private static final long serialVersionUID = -4075985042175162396L;

	private Double value;
	
	@Column(name = "DOUBLE_VALUE")
	private Double getDoubleValue() {
		return value;
	}

	private void setDoubleValue(Double value) {
		this.value = value;
	}

	@Transient
	public Double getValue() {
		return getDoubleValue();
	}

	public void setValue(Double value) {
		setDoubleValue(value);
	}
	
	
}
