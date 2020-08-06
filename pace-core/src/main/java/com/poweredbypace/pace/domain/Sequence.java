package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APP_SEQUENCE")
public class Sequence extends BaseEntity {
	
	private static final long serialVersionUID = 7148852176080035336L;
	
	private String code;
	private Long value;
	private Long step;
	
	@Column(name="CODE", unique=true)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="VALUE")
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	
	@Column(name="STEP")
	public Long getStep() {
		return step;
	}
	public void setStep(Long step) {
		this.step = step;
	}
	

}
