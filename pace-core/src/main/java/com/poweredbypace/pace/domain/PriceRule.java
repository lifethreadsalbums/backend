package com.poweredbypace.pace.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "APP_PRICE_RULE")
public class PriceRule extends BaseEntity {
	
	private static final long serialVersionUID = 7321558428819902109L;
	
	public static enum PriceRuleType { 
		LineItemRule, 	//price rule gets applied to a single line item 
		LineItemGroupingRule, 	//price rule gets applied to a single line item, can group several line items together
		CartRule 		//price rule gets applied to the whole cart, ie. rush rule 
	}
	
	private String code;
	private String conditionExpression;
	private String quantityExpression;
	private String priceExpression;
	private List<String> group;
	private PriceRuleType type;
	private TResource label;
	private Boolean labelIsExpression;
	private Integer order;
	private String description;
	
	@Column(name = "QUANTITY_EXPRESSION", columnDefinition="TEXT")
	public String getQuantityExpression() {
		return quantityExpression;
	}
	public void setQuantityExpression(String quantityExpression) {
		this.quantityExpression = quantityExpression;
	}
	
	@Column(name = "PRICE_EXPRESSION", columnDefinition="TEXT")
	public String getPriceExpression() {
		return priceExpression;
	}
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	public PriceRuleType getType() {
		return type;
	}
	public void setType(PriceRuleType type) {
		this.type = type;
	}
	
	@ElementCollection
	@CollectionTable(name="APP_PRICE_RULE_GROUP", joinColumns= @JoinColumn(name="PRICE_RULE_ID"))
	@Column(name="CODE")
	public List<String> getGroup() {
		return group;
	}
	public void setGroup(List<String> group) {
		this.group = group;
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
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
