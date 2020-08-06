package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_PACKAGE")
public class TShippingPackageType extends BaseTerm {

	private static final long serialVersionUID = -3630160986451101418L;

	private Integer length;
	private Integer width;
	private Integer height;
	private Integer maxQuantity;
	
	@Column(name = "LENGTH")
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	@Column(name = "WIDTH")
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@Column(name = "MAX_QTY")
	public Integer getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	
}
