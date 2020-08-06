package com.poweredbypace.pace.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.store.Store;

@Entity
@Table(name = "T_RESOURCE_TRANSLATION",
		uniqueConstraints = @UniqueConstraint(columnNames={"T_RESOURCE_ID", "LANG", "STORE_ID", "VIEW_ID"}))
public class TResourceTranslation extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7285478323264916372L;

	private TResource resource;

	private String value;
	private String description;
	private String language;
	private Store store;
	private View view;
	
	@ManyToOne
	@JoinColumn(name = "T_RESOURCE_ID")
	@JsonIgnore
	public TResource getResource() {
		return resource;
	}

	public void setResource(TResource resource) {
		this.resource = resource;
	}

	@Column(name = "VALUE", columnDefinition="TEXT")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "LANG")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	@ManyToOne
	@JoinColumn(name = "VIEW_ID", nullable=true)
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
}