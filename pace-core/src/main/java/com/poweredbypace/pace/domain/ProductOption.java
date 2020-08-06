package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "P_PRODUCT_OPTION")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ProductOption<T> extends BaseEntity {

	private static final long serialVersionUID = 1393006776930978168L;
	
	private PrototypeProductOption prototypeProductOption;
	private Product product;
	private boolean required;
	
	@Transient
	public abstract T getValue();
	public abstract void setValue(T value);

	@JoinColumn(name = "PRODUCT_ID", nullable=false)
	@ManyToOne
	@JsonIgnore
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Transient
	public String getDisplayName() {
		if (getPrototypeProductOption().getLabel()!=null)
			return getPrototypeProductOption().getLabel().getTranslatedValue();
		else
			return getPrototypeProductOption().getProductOptionType().getDisplayName();
	}
	
	@Transient
	public String getDisplayValue() {
		if (getValue()!=null)
			return getValue().toString();
		return null;
	}

	@JoinColumn(name = "PROTOTYPE_PRODUCT_OPTION_ID")
	@ManyToOne
	public PrototypeProductOption getPrototypeProductOption() {
		return prototypeProductOption;
	}
	public void setPrototypeProductOption(
			PrototypeProductOption prototypeProductOption) {
		this.prototypeProductOption = prototypeProductOption;
	}
	
	@Column(name = "IS_REQUIRED", columnDefinition = "TINYINT(1)")
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getVersion())
			.append(getClass().getSimpleName())
			.append(getPrototypeProductOption().hashCode())
			.toHashCode();
	}

	@Override
	public boolean equals(Object o){
		if( o == null)
		    return false;
		
		if (getClass() != o.getClass())
			return false;
		  
		ProductOption<?> that = (ProductOption<?>)o;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getId(), that.getId());
		b.append(getVersion(), that.getVersion());
		b.append(getPrototypeProductOption(), that.getPrototypeProductOption());
		
		return b.isEquals();
	}
}
