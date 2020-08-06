package com.poweredbypace.pace.domain.settings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleProductSerializer;
import com.poweredbypace.pace.json.SimpleUserSerializer;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "APP_SETTINGS")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class Settings extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 885615463459254554L;
	
	private Store store;
	private Product product;
	private User user;
	private String settingsAsString;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID", nullable = true)
	@JsonIgnore
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	@JsonSerialize(using=SimpleProductSerializer.class)
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}
	public void setUser(final User user) {
		this.user = user;
	}
	
	@Column(name = "SETTINGS", columnDefinition="TEXT")
	@JsonIgnore
	public String getSettingsAsString() {
		return settingsAsString;
	}
	public void setSettingsAsString(String settingsAsString) {
		this.settingsAsString = settingsAsString;
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public Map<String, Object> getSettings() {
		if(getSettingsAsString() != null) {
			return (Map<String, Object>)JsonUtil.deserialize(getSettingsAsString(), Map.class);
		} else {
			return new HashMap<String, Object>();
		}
	}
	public void setSettings(Map<String, Object> params) {
		setSettingsAsString(JsonUtil.serialize(params));
	}

}
