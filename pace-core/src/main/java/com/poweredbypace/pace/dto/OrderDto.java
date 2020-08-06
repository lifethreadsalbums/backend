package com.poweredbypace.pace.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.shipping.ShippingOption;

@JsonIgnoreProperties(ignoreUnknown=true) 
public class OrderDto {
	
	private Long id;
	private List<Address> addresses;
	private String couponCode;
	private ShippingOption shippingOption;
	private String notes;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public ShippingOption getShippingOption() {
		return shippingOption;
	}
	public void setShippingOption(ShippingOption shippingOption) {
		this.shippingOption = shippingOption;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
