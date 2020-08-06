package com.poweredbypace.pace.payment.payeezy;

public class SoftDescriptorInfo {

	String dbaName; 			//Business name
	String merchantContactInfo; //Business contact information
	String street;				//Business street
	String city; 				//Business city
	String region; 				//Business region
	String countryCode;			//Business country
	String postalCode;			//Business postal/zip code
	String mid;					//Business MID number
	String mcc;					//Business MCC number
	
	public String getDbaName() {
		return dbaName;
	}
	public void setDbaName(String dbaName) {
		this.dbaName = dbaName;
	}
	public String getMerchantContactInfo() {
		return merchantContactInfo;
	}
	public void setMerchantContactInfo(String merchantContactInfo) {
		this.merchantContactInfo = merchantContactInfo;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	
}
