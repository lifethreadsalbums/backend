package com.poweredbypace.pace.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "T_PRODUCT_OPTION_VALUE")
@JsonIgnoreProperties({"id", "version", "name", "description"})
public class TProductOptionValue extends BaseTerm {

	private static final long serialVersionUID = 336621652287505010L;

	private TProductOptionType productOptionType;
	private String code;
	private String paramsAsString;
	
	@Column(name = "PARAMS", columnDefinition="TEXT")
	@JsonIgnore
	public String getParamsAsString() {
		return paramsAsString;
	}
	public void setParamsAsString(String paramsAsString) {
		this.paramsAsString = paramsAsString;
	}

	@SuppressWarnings("unchecked")
	@Transient
	public Map<String, Object> getParams() {
		if(getParamsAsString() != null) {
			return (Map<String, Object>)JsonUtil.deserialize(getParamsAsString(), Map.class);
		} else {
			return new HashMap<String, Object>();
		}
	}
	public void setParams(Map<String, Object> params) {
		setParamsAsString(JsonUtil.serialize(params));
	}
	
	@JoinColumn(name = "T_PRODUCT_OPTION_TYPE")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	public TProductOptionType getProductOptionType() {
		return productOptionType;
	}

	public void setProductOptionType(TProductOptionType productOptionType) {
		this.productOptionType = productOptionType;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}