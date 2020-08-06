package com.poweredbypace.pace.domain.shipping;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.TShippingPackageType;
import com.poweredbypace.pace.domain.order.OrderItem;

@Entity
@Table(name = "O_SHIPPING_PACKAGE")
public class ShippingPackage extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5531686497892661110L;
	
	private Shipment shipment;
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	private TShippingPackageType packageType;
	private Float weight;
	private String trackingId;

	public ShippingPackage() { }
	

	@ManyToOne
	@JoinColumn(name = "SHIPMENT_ID")
	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "O_SHIPPING_PACKAGE_ITEM", 
		joinColumns = {	@JoinColumn(name = "PACKAGE_ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "ORDER_ITEM_ID", nullable = false, updatable = false) })
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@ManyToOne
	@JoinColumn(name = "PACKAGE_TYPE_ID")
	public TShippingPackageType getPackageType() {
		return packageType;
	}

	public void setPackageType(TShippingPackageType packageType) {
		this.packageType = packageType;
	}
	
	@Column(name = "TRACKING_ID")
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	@Column(name = "WEIGHT")
	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	@Transient
	public Integer getLength() {
		return packageType.getLength();
	}

	@Transient
	public Integer getWidth() {
		return packageType.getWidth();
	}

	@Transient
	public Integer getHeight() {
		return packageType.getHeight();
	}

	
}