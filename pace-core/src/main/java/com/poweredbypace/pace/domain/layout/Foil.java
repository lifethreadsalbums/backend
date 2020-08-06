package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="APP_FOIL")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class Foil extends BaseEntity {

	private static final long serialVersionUID = -2328330814948844160L;

	String code;
	String color;
	String textureUrl;
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="COLOR")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Column(name="TEXTURE_URL")
	public String getTextureUrl() {
		return textureUrl;
	}
	public void setTextureUrl(String textureUrl) {
		this.textureUrl = textureUrl;
	}
	
}
