package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="APP_API_KEY")
public class ApiKey extends BaseEntity {

	private static final long serialVersionUID = -484054143682649594L;
	
	private String username;
	private String password;
	private String description;
	
	@Column(name="NAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="API_KEY")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
