package com.poweredbypace.pace.domain.shipping;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.util.NameUtil;

@Entity
@Table(name = "O_SHIPMENT_ADDRESS")
public class ShipmentAddress extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -117399825540928925L;

	private String companyName;
	private String receiverFirstName;
	private String receiverLastName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String email;
	private String phone;

	public ShipmentAddress() {
		
	}
	
	public ShipmentAddress(Address address) {
		this.companyName = address.getCompanyName();
		this.receiverFirstName = address.getFirstName();
		this.receiverLastName = address.getLastName();
		this.addressLine1 = address.getAddressLine1();
		this.addressLine2 = address.getAddressLine2();
		this.city = address.getCity();
		if(address.getState().getStateCode() != null) {
			this.state = address.getState().getStateCode();
		} else {
			this.state = address.getState().getName();
		}
		this.country = address.getCountry().getIsoCountryCode();
		this.zipCode = address.getZipCode();
		this.email = address.getEmail();
		this.phone = address.getPhone();
	}
	
	@Column(name = "COMPANY_NAME")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "FIRST_NAME")
	public String getReceiverFirstName() {
		return receiverFirstName;
	}

	public void setReceiverFirstName(String receiverFirstName) {
		this.receiverFirstName = receiverFirstName;
	}

	@Column(name = "LAST_NAME")
	public String getReceiverLastName() {
		return receiverLastName;
	}

	public void setReceiverLastName(String receiverLastName) {
		this.receiverLastName = receiverLastName;
	}

	@Column(name = "ADDRESS_LINE_1")
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	@Column(name = "ADDRESS_LINE_2")
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "COUNTRY")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "ZIP_CODE")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Transient
	public String getFullName() {
		return NameUtil.getFullName(getReceiverFirstName(), getReceiverLastName(), getCompanyName());
	}
}