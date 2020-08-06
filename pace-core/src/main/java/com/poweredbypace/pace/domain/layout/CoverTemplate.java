package com.poweredbypace.pace.domain.layout;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.json.SimplePrototypeProductSerializer;

@Entity
@Table(name="APP_COVER_TEMPLATE")
public class CoverTemplate extends BaseEntity {

	private static final long serialVersionUID = -198078768792403996L;
	
	private CoverType coverType;
	private PrototypeProduct prototypeProduct;
	private LayoutSize layoutSize;
	private Layout layout;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COVER_TYPE_ID")
	public CoverType getCoverType() {
		return coverType;
	}
	public void setCoverType(CoverType coverType) {
		this.coverType = coverType;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROTOTYPE_PRODUCT_ID")
	@JsonSerialize(using=SimplePrototypeProductSerializer.class)
	public PrototypeProduct getPrototypeProduct() {
		return prototypeProduct;
	}
	public void setPrototypeProduct(PrototypeProduct prototypeProduct) {
		this.prototypeProduct = prototypeProduct;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_SIZE_ID")
	public LayoutSize getLayoutSize() {
		return layoutSize;
	}
	public void setLayoutSize(LayoutSize layoutSize) {
		this.layoutSize = layoutSize;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_ID")
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
