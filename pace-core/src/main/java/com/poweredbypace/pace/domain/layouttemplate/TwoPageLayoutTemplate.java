package com.poweredbypace.pace.domain.layouttemplate;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@Entity
@DiscriminatorValue("TwoPageLayoutTemplate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwoPageLayoutTemplate extends LayoutTemplate {
	private static final long serialVersionUID = -9152039247035342063L;

	private LayoutTemplate left;
	private LayoutTemplate right;
	
	public TwoPageLayoutTemplate() {
		this.target = "spread";
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "LEFT_TMPL_ID")
	public LayoutTemplate getLeft() {
		return left;
	}
	public void setLeft(LayoutTemplate left) {
		this.left = left;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "RIGHT_TMPL_ID")
	public LayoutTemplate getRight() {
		return right;
	}
	public void setRight(LayoutTemplate right) {
		this.right = right;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(TwoPageLayoutTemplate.class.getSimpleName())
				.add("id", getId())
				.add("publicTemplate", publicTemplate)
				.add("left", left)
				.add("right", right).toString();
	}
}
