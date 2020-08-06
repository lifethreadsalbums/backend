package com.poweredbypace.pace.domain;

import java.util.Date;
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
@Table(name = "APP_DISCOUNT_RULE")
public class DiscountRule extends BaseEntity {
	
	private static final long serialVersionUID = -4188376406087574872L;
	
	public static enum DiscountRuleType {
		LineItemDiscount, OrderDiscount, ProductDiscount
	}
	
	public static enum CouponType {
		NoCoupon, SpecificCoupon
	}
	
	private String conditionExpression;
	private Discount discount;
	private DiscountRuleType type;
	private List<String> lineItemCodes;
	private CouponType couponType;
	private String couponCode;
	private Integer usesPerCustomer;
	private Integer usesPerCoupon;
	private Date fromDate;
	private Date toDate;
	private TResource label;
	private String description;
	
	@Column(name = "CONDITION_EXPRESSION", columnDefinition = "TEXT")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@ManyToOne
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	public TResource getLabel() {
		return label;
	}
	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DISCOUNT_ID", nullable = false)
	public Discount getDiscount() {
		return discount;
	}
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING) 
	public DiscountRuleType getType() {
		return type;
	}
	public void setType(DiscountRuleType type) {
		this.type = type;
	}
	
	@ElementCollection
	@CollectionTable(name="APP_DISCOUNT_RULE_LINE_ITEM_CODE", joinColumns= @JoinColumn(name="DISCOUNT_RULE_ID"))
	@Column(name="CODE")
	public List<String> getLineItemCodes() {
		return lineItemCodes;
	}
	public void setLineItemCodes(List<String> lineItemCodes) {
		this.lineItemCodes = lineItemCodes;
	}
	
	@Column(name = "COUPON_TYPE")
	@Enumerated(EnumType.STRING)
	public CouponType getCouponType() {
		return couponType;
	}
	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}
	
	@Column(name = "COUPON_CODE")
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	@Column(name = "USES_PER_CUSTOMER")
	public Integer getUsesPerCustomer() {
		return usesPerCustomer;
	}
	public void setUsesPerCustomer(Integer usesPerCustomer) {
		this.usesPerCustomer = usesPerCustomer;
	}
	
	@Column(name = "USES_PER_COUPON")
	public Integer getUsesPerCoupon() {
		return usesPerCoupon;
	}
	public void setUsesPerCoupon(Integer usesPerCoupon) {
		this.usesPerCoupon = usesPerCoupon;
	}
	
	@Column(name = "FROM_DATE")
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(name = "TO_DATE")
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
