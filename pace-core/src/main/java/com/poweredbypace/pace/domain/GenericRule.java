package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "APP_GENERIC_RULE")
public class GenericRule extends BaseEntity {

	public static final String SPINE_WIDTH = "SPINE_WIDTH";
	public static final String HINGE_GAP = "HINGE_GAP";
	public static final String PRINT_DUPLICATE = "PRINT_DUPLICATE";
	public static final String LAY_FLAT_LAYOUT = "LAY_FLAT_LAYOUT";
	public static final String CENTER_OFFSET = "CENTER_OFFSET";
	public static final String LAYOUT_SIZE = "LAYOUT_SIZE";
	public static final String FREE_SHIPPING = "FREE_SHIPPING";
	
	private static final long serialVersionUID = 3274938763095355202L;

	private String code;
	private String conditionExpression;
	private String jsonData;
	private String description;
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "CONDITION_EXPRESSION", columnDefinition = "TEXT")
	public String getConditionExpression() {
		return conditionExpression;
	}
	
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@Column(name = "JSON_DATA", columnDefinition = "TEXT")
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
