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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.json.ProductSerializer;
import com.poweredbypace.pace.json.View;


@Entity
@Table(
	name = "O_ORDER_ITEM",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"ORDER_ID" , "PRODUCT_ID"})}
)
public class OrderItem extends BaseEntity {
	
	private static final long serialVersionUID = 6880599076568265241L;

	private Order order;
	private Product product;
	private Money subtotal;
	private String orderItemNumber;
	private Integer listOrder;
	
	@Column(name="LIST_ORDER")
	public Integer getListOrder() {
		return listOrder;
	}
	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}
	
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
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	@JsonSerialize(using=ProductSerializer.class)
	@JsonView(View.OrderShortInfo.class)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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
	
	@Column(name="ORDER_ITEM_NUMBER")
	public String getOrderItemNumber() {
		return orderItemNumber;
	}
	public void setOrderItemNumber(String orderItemNumber) {
		this.orderItemNumber = orderItemNumber;
	}
	

}
