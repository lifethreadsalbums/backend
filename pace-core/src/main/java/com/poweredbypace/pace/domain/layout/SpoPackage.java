package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.user.User;

@Entity
@Table(name="P_SPO_PACKAGE",
	uniqueConstraints={
	    @UniqueConstraint(columnNames = {"USER_ID", "NAME"})
	}
)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SpoPackage extends BaseEntity {
	
	private static final long serialVersionUID = -7454224701299378993L;
	
	private User user;
	private String dataJson;
	private String name;
	
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "DATA_JSON", columnDefinition = "LONGTEXT")
	public String getDataJson() {
		return dataJson;
	}
	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

}
