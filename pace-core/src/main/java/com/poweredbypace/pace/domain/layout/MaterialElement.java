package com.poweredbypace.pace.domain.layout;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.json.SimplePrototypeProductOptionSerializer;


@Entity
@DiscriminatorValue("MaterialElement")
public class MaterialElement extends Element {

	private static final long serialVersionUID = -2952819571053599455L;
	
	private PrototypeProductOption prototypeProductOption;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROTOTYPE_PRODUCT_OPTION_ID")
	@JsonSerialize(using=SimplePrototypeProductOptionSerializer.class)
	public PrototypeProductOption getPrototypeProductOption() {
		return prototypeProductOption;
	}

	public void setPrototypeProductOption(
			PrototypeProductOption prototypeProductOption) {
		this.prototypeProductOption = prototypeProductOption;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst.getClass().isAssignableFrom(MaterialElement.class)) {
			((MaterialElement)dst).setPrototypeProductOption(prototypeProductOption);
		}
	}
	
}
