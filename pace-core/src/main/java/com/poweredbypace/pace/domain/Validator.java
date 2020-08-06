package com.poweredbypace.pace.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.validator.ProductOptionValidator;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "VALIDATOR")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class Validator extends BaseEntity {

	private static final long serialVersionUID = -36188714959189580L;

	private TValidator tValidator;
	private String paramsAsString;
	private TProductOptionType tProductOptionType;
	private PrototypeProductOption prototypeProductOption;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "T_VALIDATOR_ID")
	public TValidator gettValidator() {
		return tValidator;
	}
	public void settValidator(TValidator tValidator) {
		this.tValidator = tValidator;
	}
	
	@Column(name = "PARAMS")
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

	@Transient
	public ProductOptionValidator getConcreteValidator() throws InstantiationException, IllegalAccessException {
		return (ProductOptionValidator) gettValidator().getConcreteValidatorClass().newInstance();
	}
	
	public boolean validate(ProductOption<?> productOption) throws InstantiationException, IllegalAccessException {
		return getConcreteValidator().validate(productOption, getParams());
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "T_PROD_OPT_TYPE_ID")
	public TProductOptionType gettProductOptionType() {
		return tProductOptionType;
	}
	public void settProductOptionType(TProductOptionType tProductOptionType) {
		this.tProductOptionType = tProductOptionType;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "PROT_PROD_OPT_ID")
	public PrototypeProductOption getPrototypeProductOption() {
		return prototypeProductOption;
	}
	public void setPrototypeProductOption(
			PrototypeProductOption prototypeProductOption) {
		this.prototypeProductOption = prototypeProductOption;
	}
}