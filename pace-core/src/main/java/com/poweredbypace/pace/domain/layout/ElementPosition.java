package com.poweredbypace.pace.domain.layout;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="APP_ELEMENT_POSITION")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class ElementPosition extends BaseEntity {

	private static final long serialVersionUID = 4628463473932280364L;

	private String code;
	private Float top;
	private Float bottom;
	private Float left;
	private Float right;
	private Float centerX;
	private Float centerY;
	private LayoutSize layoutSize;
	private ElementPosition parent;
	private List<ElementPosition> variants;
	private Boolean isDefault;
	
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="P_TOP")
	public Float getTop() {
		return top;
	}
	public void setTop(Float top) {
		this.top = top;
	}
	
	@Column(name="P_BOTTOM")
	public Float getBottom() {
		return bottom;
	}
	public void setBottom(Float bottom) {
		this.bottom = bottom;
	}
	
	@Column(name="P_LEFT")
	public Float getLeft() {
		return left;
	}
	public void setLeft(Float left) {
		this.left = left;
	}
	
	@Column(name="P_RIGHT")
	public Float getRight() {
		return right;
	}
	public void setRight(Float right) {
		this.right = right;
	}
	
	@Column(name="P_CENTER_X")
	public Float getCenterX() {
		return centerX;
	}
	public void setCenterX(Float centerX) {
		this.centerX = centerX;
	}
	
	@Column(name="P_CENTER_Y")
	public Float getCenterY() {
		return centerY;
	}
	public void setCenterY(Float centerY) {
		this.centerY = centerY;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_SIZE_ID", nullable=true)
	public LayoutSize getLayoutSize() {
		return layoutSize;
	}
	public void setLayoutSize(LayoutSize layoutSize) {
		this.layoutSize = layoutSize;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID", nullable=true)
	public ElementPosition getParent() {
		return parent;
	}
	public void setParent(ElementPosition parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy = "parent")
	public List<ElementPosition> getVariants() {
		return variants;
	}
	public void setVariants(List<ElementPosition> variants) {
		this.variants = variants;
	}
	
	@Column(name="IS_DEFAULT", columnDefinition = "TINYINT(1)")
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	
}
