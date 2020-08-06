package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "T_STATE")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class TState extends BaseTerm {

	private static final long serialVersionUID = 8251055883967730941L;
	
	private TCountry country;
	private String stateCode;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "T_COUNTRY_ID", nullable = false)
	public TCountry getCountry() {
		return country;
	}

	public void setCountry(TCountry country) {
		this.country = country;
	}
	
	@Override
    public boolean equals(Object o) {
		
        if (this == o) return true;
        if (o == null || !(o instanceof TState))
            return false;

        TState other = (TState)o;

        if (this.getId() == null || other.getId() == null)
            return false;

        return getId().equals(other.getId());
	}

	@Column(name = "STATE_CODE")
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
}
