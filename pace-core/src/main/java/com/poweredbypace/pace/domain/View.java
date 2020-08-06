package com.poweredbypace.pace.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="APP_VIEW")
public class View extends BaseEntity {

	private static final long serialVersionUID = 6245158715249231607L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
