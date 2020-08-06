package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_PRODUCT_OPTION_BOOL")
public class ProductOptionBoolean extends ProductOption<Boolean> {

	private static final long serialVersionUID = -3455499876821150788L;
	
	private Boolean value;
	
	@Column(name = "BOOL_VALUE", columnDefinition = "TINYINT(1)")
	private Boolean getBoolValue() {
		return value;
	}

	private void setBoolValue(Boolean value) {
		this.value = value;
	}

	@Transient
	public Boolean getValue() {
		return getBoolValue();
	}

	public void setValue(Boolean value) {
		setBoolValue(value);
	}
}
