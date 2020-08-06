package com.poweredbypace.pace.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name = "APP_ROLE")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Role extends BaseEntity implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = -2399227819111528179L;
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	public static final String ROLE_PROOFER_USER = "ROLE_PROOFER_USER";

	private String name;
	private String description;
	
	@Override
	@Transient
	@JsonIgnore
	public String getAuthority() {
		return getName();
	}

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

}
