package com.poweredbypace.pace.domain.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Address.AddressType;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;
import com.poweredbypace.pace.json.View;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "O_ORDER")
public class Order extends BaseEntity {

	public enum OrderState {
		Pending,
		PaymentComplete,
		Shipped,
		Completed,
		Canceled,
		OnHold,
	}
	
	private static final long serialVersionUID = -152140650097906309L;

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private List<OrderAdjustment> orderAdjustments = new ArrayList<OrderAdjustment>();
	private Store store;
	private Money subtotal;
	private Money subtotalIncludingAdjustments;
	private Money total;
	private Money shippingCost;
	private Boolean shippingIncludedInTax = false;
	private String couponCode;
	private List<OrderTax> taxes = new ArrayList<OrderTax>();
	private List<Address> addresses = new ArrayList<Address>();
	private User user;
	private OrderState state;
	private boolean dropShipment;
	private Date dateCreated;
	private String internalId;
	private Boolean freeShipping;
	private String shippingOptionSerialized;
	private String orderNumber;
	private Boolean isSplit;
	
	@Column(name="STATE")
	@Enumerated(EnumType.STRING)
	@JsonView(View.OrderShortInfo.class)
	public OrderState getState() {
		return state;
	}
	public void setState(OrderState state) {
		this.state = state;
	}
	
	@JsonView(View.OrderShortInfo.class)
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "order", 
		cascade=CascadeType.ALL,
		orphanRemoval=true
	)
	@OrderBy("listOrder")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "order", 
		cascade=CascadeType.ALL,
		orphanRemoval = true
	)
	public List<OrderAdjustment> getOrderAdjustments() {
		return orderAdjustments;
	}
	public void setOrderAdjustments(List<OrderAdjustment> orderAdjustments) {
		this.orderAdjustments = orderAdjustments;
	}
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "order", 
		cascade=CascadeType.ALL,
		orphanRemoval = true
	)
	public List<OrderTax> getTaxes() {
		return taxes;
	}
	public void setTaxes(List<OrderTax> taxes) {
		this.taxes = taxes;
	}
	
	@Column(name = "COUPON_CODE")
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	@JsonIgnore
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	
	/**
	 * Returns the subtotal price for the order.  The subtotal price is the price of all order items
     * with item offers applied.  The subtotal does not take into account the order promotions, shipping costs or any
     * taxes that apply to this order.
     *
     * @return the total item price with offers applied
	 */
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
	
	/**
	 * Returns the order subtotal which is the price of all order items including 
	 * adjustments such as RUSH. No shipping costs nor taxes are included. 
	 * @return the order subtotal
	 */
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SUBTOTAL_INCLUDING_ADJUSTMENTS_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SUBTOTAL_INCLUDING_ADJUSTMENTS_AMOUNT")) ,
		})
	public Money getSubtotalIncludingAdjustments() {
		return subtotalIncludingAdjustments;
	}
	public void setSubtotalIncludingAdjustments(Money subtotalIncludingAdjustments) {
		this.subtotalIncludingAdjustments = subtotalIncludingAdjustments;
	}
	
	/**
	 * Returns the grand total, including adjustments, shipping costs and taxes
	 * @return
	 */
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "TOTAL_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "TOTAL_AMOUNT")) ,
		})
	@JsonView(View.OrderShortInfo.class)
	public Money getTotal() {
		return total;
	}
	public void setTotal(Money total) {
		this.total = total;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SHIPPING_COST_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SHIPPING_COST_AMOUNT")) ,
		})
	public Money getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(Money shippingCost) {
		this.shippingCost = shippingCost;
	}
	
	@Column(name="SHIPPING_INCLUDED_IN_TAX", columnDefinition = "TINYINT(1)")
	public Boolean getShippingIncludedInTax() {
		return shippingIncludedInTax;
	}
	public void setShippingIncludedInTax(Boolean shippingIncludedInTax) {
		this.shippingIncludedInTax = shippingIncludedInTax;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name = "O_ORDER_ADDRESS", 
		joinColumns = {	@JoinColumn(name = "ORDER_ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "ADDRESS_ID", nullable = false, updatable = false) })
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	@Column(name="DATE_CREATED")
	@JsonView(View.OrderShortInfo.class)
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Column(name="ORDER_NUMBER")
	@JsonView(View.OrderShortInfo.class)
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@Transient
	public Address getAddress(AddressType type)
	{
		for(Address address:getAddresses())
		{
			if (address.getAddressType()==type)
				return address;
		}
		return null;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonSerialize(using=SimpleUserSerializer.class)
	@JsonView(View.OrderShortInfo.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Column(name="INTERNAL_ID")
	public String getInternalId() {
		return internalId;
	}
	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}
	
	@Column(name = "DROP_SHIPMENT", columnDefinition = "TINYINT(1)")
	public boolean getDropShipment() {
		return dropShipment;
	}
	public void setDropShipment(boolean dropshipment) {
		this.dropShipment = dropshipment;
	}
	
	@Column(name = "FREE_SHIPPING", columnDefinition = "TINYINT(1)")
	public Boolean getFreeShipping() {
		return freeShipping;
	}
	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
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
	@JsonIgnore
	public boolean hasStudioSample() {
		for(OrderItem orderItem:getOrderItems()) {
			for(Product p:orderItem.getProduct().getProductAndChildren()) {
				if (BooleanUtils.isTrue(p.getStudioSample()))
					return true;
			}
		}
		return false;
	}
	
	@Transient
	//@JsonIgnore
	public ShippingOption getShippingOption() {
		return getShippingOption(ShippingOption.class);
	}
	
	public void setShippingOption(ShippingOption shippingOption) {
		if (shippingOption==null) {
			setShippingOptionSerialized(null);
		} else {
			setShippingOptionSerialized(JsonUtil.serialize(shippingOption));
		}
	}
	
	@Transient
	@JsonIgnore
	public Address getBillingAddress() {
		return getAddress(AddressType.BillingAddress);
	}
	
	@Transient
	@JsonIgnore
	public Address getShippingAddress() {
		return getAddress(AddressType.ShippingAddress);
	}
	
	@Transient
	@JsonIgnore
	public Address getDropShippingAddress() {
		return getAddress(AddressType.DropShippingAddress);
	}
	
	@Transient
	public String getTransactionOrderId() {
		if (this.getId()==null)
			return null;
		
		String orderInfo = StringUtils.join(new Object[] {
				this.getId(),
				this.getSubtotal(),
				this.getShippingCost(),
				this.getInternalId() });
		
		String hash = this.getId().toString() + ":" + DigestUtils.md5DigestAsHex(orderInfo.getBytes());
		return hash;
	}
	
	@Transient
	@JsonIgnore
	public boolean getRush() {
		boolean rush = false;
		for(OrderItem orderItem:this.getOrderItems()) {
			for(Product p:orderItem.getProduct().getProductAndChildren()) {
				if ( BooleanUtils.isTrue(p.getRush()) ) {
					rush = true;
					break;
				}
			}
			if (rush)
				break;
		}
		return rush;
	}
	
	/**
     * <p>Returns the information whether if the order has been split between different section, 
     * i.e. Some of the items has been shipped already while the other ones are being processed.</p>
     * 
     * @return <code>true</code> only if the order has been split
     */
	@Transient
	public Boolean getIsSplit() {
		return isSplit;
	}
	public void setIsSplit(Boolean isSplit) {
		this.isSplit = isSplit;
	}
	
	
	
}
