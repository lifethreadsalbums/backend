package com.poweredbypace.pace.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "T_COUNTRY")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class TCountry extends BaseTerm {

	private static final long serialVersionUID = 8251055883967730941L;
	
	private Integer countryCode;
	private String alphaCountryCode;
	private String isoCountryCode;
	private String phoneMask;
	private Set<TState> states;

	@Column(name = "COUNTRY_CODE")
	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	@OrderBy("name")
	public Set<TState> getStates() {
		return states;
	}

	public void setStates(Set<TState> states) {
		this.states = states;
	}
	
	@Column(name = "ISO_COUNTRY_CODE")
	public String getIsoCountryCode() {
		return isoCountryCode;
	}

	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}

	@Column(name = "PHONE_MASK")
	public String getPhoneMask() {
		return phoneMask;
	}

	public void setPhoneMask(String phoneMask) {
		this.phoneMask = phoneMask;
	}

	@Override
    public boolean equals(Object o) {
		
        if (this == o) return true;
        if (o == null || !(o instanceof TCountry))
            return false;

        TCountry other = (TCountry)o;

        if (this.getId() == null || other.getId() == null)
            return false;

        return getId().equals(other.getId());
	}

	@Column(name = "ALPHABETIC_COUNTRY_CODE")
	public String getAlphaCountryCode() {
		return alphaCountryCode;
	}

	public void setAlphaCountryCode(String alphaCountryCode) {
		this.alphaCountryCode = alphaCountryCode;
	}
}
