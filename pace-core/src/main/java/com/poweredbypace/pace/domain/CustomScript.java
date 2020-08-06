package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APP_CUSTOM_SCRIPT")
public class CustomScript extends BaseEntity {
	
	private static final long serialVersionUID = 4086974102688995609L;
	
	private String code;
	private String script;
	
	
	@Column(name = "CODE", nullable = false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "SCRIPT", nullable = false, columnDefinition = "TEXT")
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}

}
