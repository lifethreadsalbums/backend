package com.poweredbypace.pace.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.poweredbypace.pace.domain.layout.Element;


@Entity
@Table(name = "P_PRODUCT_OPTION_ELEMENT")
public class ProductOptionElement extends ProductOption<Element> {

	private static final long serialVersionUID = 3824569653793255589L;
	
	private Element element;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="ELEMENT_ID", nullable=true)
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Transient
	public Element getValue() {
		return getElement();
	}

	public void setValue(Element value) {
		setElement(value);
	}
}
