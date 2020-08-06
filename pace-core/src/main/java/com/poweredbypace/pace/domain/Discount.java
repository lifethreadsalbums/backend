package com.poweredbypace.pace.domain;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.poweredbypace.pace.util.SpringContextUtil;

@Entity
@Table(name = "APP_DISCOUNT")
public class Discount extends BaseEntity {

	private static final long serialVersionUID = -1810582534305853595L;

	public static enum DiscountType { Percent, FixedValue }
	
	private DiscountType type;
	private Float amount;
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	public DiscountType getType() {
		return type;
	}
	public void setType(DiscountType type) {
		this.type = type;
	}
	
	@Column(name = "AMOUNT")
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	@Transient
	public float getDiscountValue(float price) {
		return type==DiscountType.Percent ? 
				price * amount / 100.0f:
				amount;
	}
	
	@Transient
	public BigDecimal getDiscountValue(BigDecimal price) {
		return type==DiscountType.Percent ? 
				price.multiply( new BigDecimal(amount)).divide(new BigDecimal("100.0")) :
				new BigDecimal(amount);
	}
	
	@Transient
	public String getDisplayPrice() {
		Currency currency = SpringContextUtil.getEnv().getCurrency();
		return type==DiscountType.Percent ? 
				"-" + amount + "%" :
				"-" + (new Money(amount, currency.getCurrencyCode())).getDisplayPrice();
	}
}
