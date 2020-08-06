package com.poweredbypace.pace.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "APP_DESTINATION_ZONE")
public class DestinationZone extends BaseEntity {
	
	private static final long serialVersionUID = -1161926603812129216L;
	
	private String code;
	private String name;
	private TCountry country;
	private List<TState> states;
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID", nullable = false)
	public TCountry getCountry() {
		return country;
	}
	public void setCountry(TCountry country) {
		this.country = country;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_DESTINATION_ZONE_STATE", joinColumns = { 
			@JoinColumn(name = "DESTINATION_ZONE_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "STATE_ID", 
					nullable = false, updatable = false) })
	public List<TState> getStates() {
		return states;
	}
	public void setStates(List<TState> states) {
		this.states = states;
	}
	
	
	
}
