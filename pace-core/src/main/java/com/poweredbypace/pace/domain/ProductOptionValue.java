package com.poweredbypace.pace.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "P_PRODUCT_OPTION_VALUE")
public class ProductOptionValue extends ProductOption<PrototypeProductOptionValue> {

	private static final long serialVersionUID = 7374235254914614850L;

	private PrototypeProductOptionValue prototypeProductOptionValue;

	@Override
	@Transient
	public PrototypeProductOptionValue getValue() {
		return getPrototypeProductOptionValue();
	}

	@Override
	public void setValue(PrototypeProductOptionValue value) {
		setPrototypeProductOptionValue(value);
	}

	@Transient
	@JsonIgnore
	public List<PrototypeProductOptionValue> getAllowedValues() {
		return getPrototypeProductOption().getPrototypeProductOptionValues();
	}

	@ManyToOne
	@JoinColumn(name = "SELECTED_VALUE_ID")
	public PrototypeProductOptionValue getPrototypeProductOptionValue() {
		return prototypeProductOptionValue;
	}

	public void setPrototypeProductOptionValue(
			PrototypeProductOptionValue prototypeProductOptionValue) {
		this.prototypeProductOptionValue = prototypeProductOptionValue;
	}

	@Override
	@Transient
	public String getDisplayValue() {
		if (prototypeProductOptionValue!=null)
			return prototypeProductOptionValue.getProductOptionValue().getDisplayName();
		return null;
	}
	
	
}
