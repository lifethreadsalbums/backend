package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.PrototypeProductOption.SortType;
import com.poweredbypace.pace.util.UrlUtil;

@Entity
@Table(name = "P_PRODUCT_OPTION_GROUP")
public class ProductOptionGroup extends BaseEntity {

	
	private static final long serialVersionUID = -15967284224710861L;
	
	private String code;
	private TResource label;
	private TResource prompt;
	private SortType sortType;
	private Integer order;
	private String visibilityExpression;
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LABEL_RESOURCE_ID", nullable = true)
	@JsonIgnore
	public TResource getLabel() {
		return label;
	}
	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@Column(name = "VISIBILITY_EXPRESSION", columnDefinition="TEXT")
	public String getVisibilityExpression() {
		return visibilityExpression;
	}

	public void setVisibilityExpression(String visibilityExpression) {
		this.visibilityExpression = visibilityExpression;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMPT_RESOURCE_ID", nullable = true)
	@JsonIgnore
	public TResource getPrompt() {
		return prompt;
	}
	public void setPrompt(TResource prompt) {
		this.prompt = prompt;
	}
	
	@Column(name = "SORT_TYPE")
	@Enumerated(EnumType.STRING)
	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
	
	@Column(name = "GROUP_ORDER")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	@Transient
	public String getDisplayLabel() {
		return this.getLabel()!=null ? this.getLabel().getTranslatedValue() : null;
	}
	
	@Transient
	public String getDisplayPrompt() {
		return this.getPrompt()!=null ? this.getPrompt().getTranslatedValue() : null;
	}
	
	@Transient
	public String getUrl() {
		return UrlUtil.slug(getDisplayLabel());
	}

}
