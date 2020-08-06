package com.poweredbypace.pace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "APP_SHIPPING_RATE_RULE")
public class ShippingRateRule extends BaseEntity {
	
	private static final long serialVersionUID = 778052204484023633L;
	
	private String code;
	private String description;
	private String conditionExpression;
	private String rateExpression;
	private TResource label;
	private Boolean labelIsExpression;
	private Boolean enabled;
	private Integer order;
	private Date christmasShippingFrom;
	private Date christmasShippingTo;
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "RATE_EXPRESSION", columnDefinition="TEXT")
	public String getRateExpression() {
		return rateExpression;
	}
	public void setRateExpression(String rateExpression) {
		this.rateExpression = rateExpression;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	public TResource getLabel() {
		return label;
	}
	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@Column(name = "CONDITION_EXPRESSION", columnDefinition="TEXT")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@Column(name = "CHRISTMAS_SHIPPING_FROM")
	public Date getChristmasShippingFrom() {
		return christmasShippingFrom;
	}
	public void setChristmasShippingFrom(Date christmasShippingFrom) {
		this.christmasShippingFrom = christmasShippingFrom;
	}
	
	@Column(name = "CHRISTMAS_SHIPPING_TO")
	public Date getChristmasShippingTo() {
		return christmasShippingTo;
	}
	public void setChristmasShippingTo(Date christmasShippingTo) {
		this.christmasShippingTo = christmasShippingTo;
	}
	@Column(name = "SORT_ORDER")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	@Column(name = "LABEL_IS_EXPRESSION", columnDefinition = "TINYINT(1)")
	public Boolean getLabelIsExpression() {
		return labelIsExpression;
	}
	public void setLabelIsExpression(Boolean labelIsExpression) {
		this.labelIsExpression = labelIsExpression;
	}
	
	@Column(name = "ENABLED", columnDefinition = "TINYINT(1)")
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
