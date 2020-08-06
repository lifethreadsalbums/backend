package com.poweredbypace.pace.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.widget.Widget;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "T_PRODUCT_OPTION_TYPE")
public class TProductOptionType extends BaseTerm {

	private static final long serialVersionUID = 7337931089528373912L;

	private Set<PrototypeProductOption> prototypeProductOptions;
	private List<TProductOptionValue> productOptionValues;
	
	private Class<ProductOption<?>> productOptionClass;
	private SystemAttribute systemAttribute;
	private String code;
	private Set<Validator> validators;
	private String paramsAsString;
	private String defaultValue;
	
	private ProductOptionGroup group;
	private Class<Widget> ordersWidgetClass;
	private Class<Widget> buildWidgetClass;
	private Boolean includeInBuild;
	private Boolean includeInOrders;
	private TResource prompt;
	private Store store;
	
	@JoinColumn(name = "GROUP_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	public ProductOptionGroup getGroup() {
		return group;
	}

	public void setGroup(ProductOptionGroup group) {
		this.group = group;
	}
	
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

	
	@Column(name = "PRODUCT_OPTION_CLASS")
	//@JsonIgnore
	public Class<ProductOption<?>> getProductOptionClass() {
		return productOptionClass;
	}

	public void setProductOptionClass(Class<ProductOption<?>> productOptionClass) {
		this.productOptionClass = productOptionClass;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productOptionType")
	public Set<PrototypeProductOption> getPrototypeProductOptions() {
		return prototypeProductOptions;
	}

	public void setPrototypeProductOptions(
			Set<PrototypeProductOption> prototypeProductOptions) {
		this.prototypeProductOptions = prototypeProductOptions;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productOptionType")
	public List<TProductOptionValue> getProductOptionValues() {
		return productOptionValues;
	}

	public void setProductOptionValues(List<TProductOptionValue> productOptionValues) {
		this.productOptionValues = productOptionValues;
	}

	@Column(name="SYSTEM_ATTRIBUTE")
	@Enumerated(EnumType.STRING)
	public SystemAttribute getSystemAttribute() {
		return systemAttribute;
	}
	public void setSystemAttribute(SystemAttribute systemAttribute) {
		this.systemAttribute = systemAttribute;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="tProductOptionType", cascade=CascadeType.ALL)
	@JsonIgnore
	public Set<Validator> getValidators() {
		return validators;
	}

	public void setValidators(Set<Validator> validators) {
		this.validators = validators;
	}

	@JsonIgnore
	@Column(name = "ORDERS_WIDGET_CLASS")
	public Class<Widget> getOrdersWidgetClass() {
		return ordersWidgetClass;
	}

	public void setOrdersWidgetClass(Class<Widget> ordersWidgetClass) {
		this.ordersWidgetClass = ordersWidgetClass;
	}

	@JsonIgnore
	@Column(name = "BUILD_WIDGET_CLASS")
	public Class<Widget> getBuildWidgetClass() {
		return buildWidgetClass;
	}
	
	public void setBuildWidgetClass(Class<Widget> buildWidgetClass) {
		this.buildWidgetClass = buildWidgetClass;
	}

	@JsonIgnore
	@Column(name = "INCLUDE_IN_BUILD", columnDefinition = "TINYINT(1)")
	public Boolean getIncludeInBuild() {
		return includeInBuild;
	}

	public void setIncludeInBuild(Boolean includeInBuild) {
		this.includeInBuild = includeInBuild;
	}

	@JsonIgnore
	@Column(name = "INCLUDE_IN_ORDERS", columnDefinition = "TINYINT(1)")
	public Boolean getIncludeInOrders() {
		return includeInOrders;
	}

	public void setIncludeInOrders(Boolean includeInOrders) {
		this.includeInOrders = includeInOrders;
	}

	@Column(name = "DEFAULT_VALUE", columnDefinition="TEXT")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PROMPT_RESOURCE_ID")
	public TResource getPrompt() {
		return prompt;
	}

	public void setPrompt(TResource prompt) {
		this.prompt = prompt;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	
	
}