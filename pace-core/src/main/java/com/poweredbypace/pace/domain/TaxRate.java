package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="APP_TAX_RATE")
public class TaxRate extends BaseEntity {
	private static final long serialVersionUID = 7396215396390181499L;
	
	private Float rate;
	private DestinationZone destinationZone;
	private Boolean includeShipping;
	private Tax tax;
	private String conditionExpression;
	
	@Column(name = "CONDITION_EXPRESSION")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@Column(name="RATE")
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESTINATION_ZONE_ID", nullable = false)
	public DestinationZone getDestinationZone() {
		return destinationZone;
	}
	public void setDestinationZone(DestinationZone destinationZone) {
		this.destinationZone = destinationZone;
	}
	
	@Column(name="INCLUDE_SHIPPING", columnDefinition = "TINYINT(1)")
	public Boolean getIncludeShipping() {
		return includeShipping;
	}
	public void setIncludeShipping(Boolean includeShipping) {
		this.includeShipping = includeShipping;
	}
	
	@ManyToOne
	@JoinColumn(name = "TAX_ID")
	public Tax getTax() {
		return tax;
	}
	public void setTax(Tax tax) {
		this.tax = tax;
	}
	
}
