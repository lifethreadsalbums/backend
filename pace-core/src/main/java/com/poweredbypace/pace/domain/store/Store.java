package com.poweredbypace.pace.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "APP_STORE")
public class Store extends BaseEntity {

	private static final long serialVersionUID = -3897968855351485967L;

	private String code;
	private String name;
	private String domainName;
	private Boolean isDefault;
	private Address address;
	private String ownerEmail;
	private String baseCurrency;
	private String storageUrl;
	private String configJson;
	
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DOMAIN_NAME")
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@Column(name = "IS_DEFAULT", columnDefinition = "TINYINT(1)")
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Column(name = "OWNER_EMAIL")
	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "ADDRESS_ID")
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Column(name="BASE_CURRENCY", nullable=false)
	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	@Column(name="STORAGE_URL")
	public String getStorageUrl() {
		return storageUrl;
	}

	public void setStorageUrl(String storageUrl) {
		this.storageUrl = storageUrl;
	}

	@Column(name = "CONFIG_JSON", columnDefinition = "TEXT")
	public String getConfigJson() {
		return configJson;
	}

	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}
	
	@JsonIgnore
	@Transient
	public StoreConfig getConfig() {
		return JsonUtil.deserialize(this.configJson, StoreConfig.class);
	}
	
}