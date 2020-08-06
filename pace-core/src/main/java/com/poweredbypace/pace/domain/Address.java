package com.poweredbypace.pace.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.user.User;

@Entity
@Table(name = "APP_ADDRESS")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class Address extends BaseEntity implements Serializable {

	public static enum AddressType {
		BillingAddress,
		ShippingAddress,
		DropShippingAddress,
		StoreAddress
	}
	
	private static final long serialVersionUID = 2371724903360232891L;

	private String companyName;
	private String firstName;
	private String lastName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private TState state;
	private TCountry country;
	private String zipCode;
	private String email;
	private String phone;
	
	private User user;
	private AddressType addressType;
	
	public Address() {}
	
	public Address(Address address) {
		super();
		this.setFirstName(address.getFirstName());
		this.setLastName(address.getLastName());
		this.setPhone(address.getPhone());
		this.setEmail(address.getEmail());
		this.setAddressLine1(address.getAddressLine1());
		this.setAddressLine2(address.getAddressLine2());
		this.setAddressType(address.getAddressType());
		this.setCity(address.getCity());
		this.setCompanyName(address.getCompanyName());
		this.setCountry(address.getCountry());
		this.setState(address.getState());
		this.setZipCode(address.getZipCode());
	}

	public Address(AddressType addressType) {
		this.addressType = addressType;
	}
	
	@Column(name = "COMPANY_NAME")
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "ADDRESS_LINE_1")
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	@Column(name = "ADDREES_LINE_2")
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "T_STATE_ID")
	public TState getState() {
		return state;
	}
	public void setState(TState state) {
		this.state = state;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "T_COUNTRY_ID")
	public TCountry getCountry() {
		return country;
	}
	public void setCountry(TCountry country) {
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
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name="ADDRESS_TYPE")
	@Enumerated(EnumType.STRING)
	public AddressType getAddressType() {
		return addressType;
	}
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}
}
