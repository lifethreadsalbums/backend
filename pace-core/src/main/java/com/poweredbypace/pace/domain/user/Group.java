package com.poweredbypace.pace.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.json.SimpleStoreSerializer;

@Entity
@Table(name = "APP_GROUP")
public class Group extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2399227819111528179L;

	private String name;
	private String description;
	private Store store;
	
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonSerialize(using = SimpleStoreSerializer.class)
	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = false)
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}

}
