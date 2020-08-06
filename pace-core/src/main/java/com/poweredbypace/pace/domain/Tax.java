package com.poweredbypace.pace.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.Address.AddressType;

@Entity
@Table(name="APP_TAX")
public class Tax extends BaseEntity {
	private static final long serialVersionUID = 7165307777313080600L;
	
	private String name;
	private Integer priority;
	private Boolean enabled;
	private AddressType ratesDependsOn;
	private Boolean includedInProductPrice;
	private Boolean displayPriceIncludingTax;
	private List<TaxRate> taxRates;
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="PRIORITY")
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Column(name="ENABLED")
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Column(name="RATES_DEPENDS_ON")
	public AddressType getRatesDependsOn() {
		return ratesDependsOn;
	}
	public void setRatesDependsOn(AddressType ratesDependsOn) {
		this.ratesDependsOn = ratesDependsOn;
	}
	
	@Column(name="INCLUDED_IN_PRODUCT_PRICE", columnDefinition = "TINYINT(1)")
	public Boolean getIncludedInProductPrice() {
		return includedInProductPrice;
	}
	public void setIncludedInProductPrice(Boolean includedInProductPrice) {
		this.includedInProductPrice = includedInProductPrice;
	}
	
	@Column(name="DISPLAY_PRICE_INCLUDING_TAX", columnDefinition = "TINYINT(1)")
	public Boolean getDisplayPriceIncludingTax() {
		return displayPriceIncludingTax;
	}
	public void setDisplayPriceIncludingTax(Boolean displayPriceIncludingTax) {
		this.displayPriceIncludingTax = displayPriceIncludingTax;
	}
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tax")
	public List<TaxRate> getTaxRates() {
		return taxRates;
	}
	public void setTaxRates(List<TaxRate> taxRates) {
		this.taxRates = taxRates;
	}
	
	
}
