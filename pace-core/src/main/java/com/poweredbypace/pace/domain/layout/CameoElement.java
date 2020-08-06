package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("CameoElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class CameoElement extends ImageElement {

	private static final long serialVersionUID = -7174489022675024844L;
	private Float left;
	private Float top;
	private Float right;
	private Float bottom;
	private Float centerX;
	private Float centerY;
	
	@Column(name="POS_LEFT")
	public Float getLeft() {
		return left;
	}
	public void setLeft(Float left) {
		this.left = left;
	}
	
	@Column(name="POS_TOP")
	public Float getTop() {
		return top;
	}
	public void setTop(Float top) {
		this.top = top;
	}
	
	@Column(name="POS_RIGHT")
	public Float getRight() {
		return right;
	}
	public void setRight(Float right) {
		this.right = right;
	}
	
	@Column(name="POS_BOTTOM")
	public Float getBottom() {
		return bottom;
	}
	public void setBottom(Float bottom) {
		this.bottom = bottom;
	}
	
	@Column(name="POS_CENTER_X")
	public Float getCenterX() {
		return centerX;
	}
	public void setCenterX(Float centerX) {
		this.centerX = centerX;
	}
	
	@Column(name="POS_CENTER_Y")
	public Float getCenterY() {
		return centerY;
	}
	public void setCenterY(Float centerY) {
		this.centerY = centerY;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof CameoElement) {
			CameoElement value = (CameoElement)dst;
			value.setLeft(left);
			value.setTop(top);
			value.setRight(right);
			value.setBottom(bottom);
			value.setCenterX(centerX);
			value.setCenterY(centerY);
		}
	}
		
}
