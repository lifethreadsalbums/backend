package com.poweredbypace.pace.domain.shipping;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "O_SHIPMENT")
public class Shipment extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2209499905662458036L;

	public enum ShipmentStatus {
		New,
		Registered,
		Canceled,
		Closed
	}
	
	private Order order;
	private Store store;
	private ShipmentAddress billingAddress;
	private ShipmentAddress toAddress;
	private ShipmentAddress fromAddress;
	private ShipmentStatus shipmentStatus;
	private List<ShippingPackage> packages;
	private User user;
	private String shippingProviderId;
	private String shippingOptionSerialized;
	private String shipmentId;
	private boolean dropShipment;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "BILLING_ADDRESS")
	public ShipmentAddress getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(ShipmentAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "TO_ADDRESS")
	public ShipmentAddress getToAddress() {
		return toAddress;
	}
	public void setToAddress(ShipmentAddress toAddress) {
		this.toAddress = toAddress;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "FROM_ADDRESS")
	public ShipmentAddress getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(ShipmentAddress fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}
	public void setShipmentStatus(ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shipment", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	public List<ShippingPackage> getPackages() {
		return packages;
	}
	public void setPackages(List<ShippingPackage> packages) {
		this.packages = packages;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public Float getTotalWeight() {
		Float totalWeight = 0f;
		for(ShippingPackage shippingPackage : getPackages()) {
			if (shippingPackage.getWeight()!=null)
				totalWeight += shippingPackage.getWeight();
		}
		return totalWeight;
	}
	
	@Column(name = "SHIPPING_OPTION")
	protected String getShippingOptionSerialized() {
		return shippingOptionSerialized;
	}
	protected void setShippingOptionSerialized(String shippingOptionSerialized) {
		this.shippingOptionSerialized = shippingOptionSerialized;
	}
	
	@Transient
	public ShippingOption getShippingOption(Class<? extends ShippingOption> clazz) {
		String serializedObject = getShippingOptionSerialized();
		if(serializedObject != null) {
			return (ShippingOption) JsonUtil.deserialize(serializedObject, clazz);
		}
		return null;
	}
	
	@Transient
	public ShippingOption getShippingOption() {
		return getShippingOption(ShippingOption.class);
	}
	
	public void setShippingOption(ShippingOption shippingOption) {
		setShippingOptionSerialized(JsonUtil.serialize(shippingOption));
	}

	@Column(name = "SHIPPING_PROVIDER")
	public String getShippingProviderId() {
		return shippingProviderId;
	}
	public void setShippingProviderId(String shippingProviderId) {
		this.shippingProviderId = shippingProviderId;
	}
	
	@Column(name = "SHIPMENT_ID")
	public String getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	@Column(name = "IS_DROP_SHIPMENT", columnDefinition = "TINYINT(1)")
	public boolean isDropShipment() {
		return dropShipment;
	}
	public void setDropShipment(boolean dropshipment) {
		this.dropShipment = dropshipment;
	}
}