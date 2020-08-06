package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "APP_ICC_PROFILE")
public class IccProfile extends BaseEntity {
	
	private static final long serialVersionUID = 7115843672714978677L;
	
	public enum ColorSpace {
		Rgb,
		Cmyk
	}
	
	private ColorSpace colorSpace;
	private String profile;
	private String code;
	private String label;
	private Boolean blackPointCompensation = true;
	private String conditionExpression;
	
	@Column(name="COLOR_SPACE")
	@Enumerated(EnumType.STRING)
	public ColorSpace getColorSpace() {
		return colorSpace;
	}
	public void setColorSpace(ColorSpace colorSpace) {
		this.colorSpace = colorSpace;
	}
	
	@Column(name="PROFILE")
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="LABEL")
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name="BPC", columnDefinition = "TINYINT(1)")
	public Boolean getBlackPointCompensation() {
		return blackPointCompensation;
	}
	public void setBlackPointCompensation(Boolean blackPointCompensation) {
		this.blackPointCompensation = blackPointCompensation;
	}
	
	@JsonIgnore
	@Column(name="CONDITION_EXPRESSION", columnDefinition="TEXT")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

}
