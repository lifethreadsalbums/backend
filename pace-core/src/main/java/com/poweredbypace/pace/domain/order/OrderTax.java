package com.poweredbypace.pace.domain.order;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.TaxRate;

@Entity
@Table(name = "O_ORDER_TAX")
public class OrderTax extends BaseEntity {
	
	private static final long serialVersionUID = -7981865924370277044L;
	
	private Order order;
	private TaxRate taxRate;
	private Money tax;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	@JsonIgnore
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAX_RATE_ID", nullable = false)
	public TaxRate getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(TaxRate taxRate) {
		this.taxRate = taxRate;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "TAX_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "TAX_AMOUNT")) ,
		})
	public Money getTax() {
		return tax;
	}
	public void setTax(Money tax) {
		this.tax = tax;
	}

}
