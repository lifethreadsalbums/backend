package com.poweredbypace.pace.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.order.LineItem;
import com.poweredbypace.pace.domain.order.OrderAdjustment;

@Entity
@Table(name="P_PRODUCT_PRICE")
public class ProductPrice extends BaseEntity {

	private static final long serialVersionUID = 9194526916630390754L;
	
	private Product product;
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	private List<OrderAdjustment> orderSubItemAdjustments = new ArrayList<OrderAdjustment>();
	private Money subtotal;
	private Money subtotalIncludingAdjustements;
	
	private String groupId;
	
	
	
	
	@Transient
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	@JsonIgnore
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@OneToMany(fetch = FetchType.LAZY, 
		mappedBy = "productPrice", 
		cascade=CascadeType.ALL,
		orphanRemoval=true
	)
	@OrderBy("order")
	public List<LineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	@OneToMany(fetch = FetchType.LAZY, 
			mappedBy = "productPrice", 
			cascade=CascadeType.ALL,
			orphanRemoval=true
		)
	public List<OrderAdjustment> getOrderSubItemAdjustments() {
		return orderSubItemAdjustments;
	}
	public void setOrderSubItemAdjustments(
			List<OrderAdjustment> orderSubItemAdjustments) {
		this.orderSubItemAdjustments = orderSubItemAdjustments;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SUBTOTAL_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SUBTOTAL_AMOUNT")) ,
		})
	public Money getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Money subtotal) {
		this.subtotal = subtotal;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SUBTOTAL_INCLUDING_ADJUSTMENTS_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SUBTOTAL_INCLUDING_ADJUSTMENTS_AMOUNT")) ,
		})
	public Money getSubtotalIncludingAdjustements() {
		return subtotalIncludingAdjustements;
	}
	public void setSubtotalIncludingAdjustements(Money subtotalIncludingAdjustements) {
		this.subtotalIncludingAdjustements = subtotalIncludingAdjustements;
	}
	
}
