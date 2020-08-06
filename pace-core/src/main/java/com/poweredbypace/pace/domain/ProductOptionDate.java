package com.poweredbypace.pace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_PRODUCT_OPTION_DATE")
public class ProductOptionDate extends ProductOption<Date> {

	private static final long serialVersionUID = 6246999044098591365L;
	
	private Date value;
	
	@Column(name = "DATE_VALUE")
	private Date getDateValue() {
		return value;
	}

	private void setDateValue(Date value) {
		this.value = value;
	}

	@Transient
	public Date getValue() {
		return getDateValue();
	}

	public void setValue(Date value) {
		setDateValue(value);
	}
}
