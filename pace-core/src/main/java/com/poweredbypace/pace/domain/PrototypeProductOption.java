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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.validator.ProductOptionMandatoryValidator;
import com.poweredbypace.pace.domain.widget.Widget;
import com.poweredbypace.pace.json.SimpleProductOptionGroupSerializer;
import com.poweredbypace.pace.json.SimplePrototypeProductOptionSerializer;
import com.poweredbypace.pace.util.JsonUtil;
import com.poweredbypace.pace.util.UrlUtil;

@Entity
@Table(name = "P_PROTOTYPE_PRODUCT_OPTION")
public class PrototypeProductOption extends BaseEntity {

	private static final long serialVersionUID = 1937060588479944472L;

	public static enum SortType {
		Default,
		AlphabeticAscending,
		AlphabeticDescending
	}
	
	private PrototypeProduct prototypeProduct;
	private TProductOptionType productOptionType;
	private List<PrototypeProductOptionValue> prototypeProductOptionValues;
	private Boolean includeAsLineItem;
	private String code;
	private TResource label;
	private TResource prompt;
	private PrototypeProductOption parent;
	private String visibilityExpression;
	private String skipExpression;
	private String enabledExpression;
	private Set<Validator> validators;
	private ProductOptionGroup group;
	private ProductOptionGroup ordersGroup;
	private Class<Widget> ordersWidgetClass;
	private Class<Widget> buildWidgetClass;
	private Boolean includeInBuild;
	private Boolean includeInOrders;
	private Boolean ordersSubpanel;
	private String defaultValue;
	private SortType sortType;
	private Boolean includeInProductInfo;
	private Boolean includeInReprint;
	private Boolean required;
	private String paramsAsString;
	private String description;
	private Integer sortOrder;
	
	
	@JoinColumn(name = "GROUP_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	public ProductOptionGroup getGroup() {
		return group;
	}

	public void setGroup(ProductOptionGroup group) {
		this.group = group;
	}
	
	@JoinColumn(name = "ORDERS_GROUP_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using = SimpleProductOptionGroupSerializer.class)
	public ProductOptionGroup getOrdersGroup() {
		return ordersGroup;
	}

	public void setOrdersGroup(ProductOptionGroup ordersGroup) {
		this.ordersGroup = ordersGroup;
	}

	@JsonIgnore
	@JoinColumn(name = "PROTOTYPE_PRODUCT")
	@ManyToOne(fetch=FetchType.LAZY)
	public PrototypeProduct getPrototypeProduct() {
		return prototypeProduct;
	}

	public void setPrototypeProduct(PrototypeProduct prototypeProduct) {
		this.prototypeProduct = prototypeProduct;
	}

	@JoinColumn(name = "T_PRODUCT_OPTION_TYPE")
	@ManyToOne
	public TProductOptionType getProductOptionType() {
		return productOptionType;
	}

	public void setProductOptionType(TProductOptionType productOptionType) {
		this.productOptionType = productOptionType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prototypeProductOption")
	@OrderBy("id")
	public List<PrototypeProductOptionValue> getPrototypeProductOptionValues() {
		return prototypeProductOptionValues;
	}

	public void setPrototypeProductOptionValues(
			List<PrototypeProductOptionValue> prototypeProductOptionValues) {
		this.prototypeProductOptionValues = prototypeProductOptionValues;
	}
	
	@Column(name="INCLUDE_AS_LINE_ITEM", columnDefinition = "TINYINT(1)")
	@JsonIgnore
	public Boolean getIncludeAsLineItem() {
		return includeAsLineItem;
	}

	public void setIncludeAsLineItem(Boolean includeAsLineItem) {
		this.includeAsLineItem = includeAsLineItem;
	}

	@Column(name="CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	public TResource getLabel() {
		return label;
	}

	public void setLabel(TResource resource) {
		this.label = resource;
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT_PROTOTYPE_OPTION_ID")
	@JsonSerialize(using = SimplePrototypeProductOptionSerializer.class)
	public PrototypeProductOption getParent() {
		return parent;
	}

	public void setParent(PrototypeProductOption parent) {
		this.parent = parent;
	}
	
	@Column(name = "VISIBILITY_EXPRESSION", columnDefinition="TEXT")
	public String getVisibilityExpression() {
		return visibilityExpression;
	}

	public void setVisibilityExpression(String visibilityExpression) {
		this.visibilityExpression = visibilityExpression;
	}
	
	@Column(name = "SKIP_EXPRESSION", columnDefinition="TEXT")
	public String getSkipExpression() {
		return skipExpression;
	}

	public void setSkipExpression(String skipExpression) {
		this.skipExpression = skipExpression;
	}
	
	@Column(name = "ENABLED_EXPRESSION", columnDefinition="TEXT")
	public String getEnabledExpression() {
		return enabledExpression;
	}

	public void setEnabledExpression(String enabledExpression) {
		this.enabledExpression = enabledExpression;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="prototypeProductOption", cascade=CascadeType.ALL)
	@JsonIgnore
	public Set<Validator> getValidators() {
		return validators;
	}

	public void setValidators(Set<Validator> validators) {
		this.validators = validators;
	}

	@Column(name = "ORDERS_WIDGET_CLASS")
	@JsonIgnore
	public Class<Widget> getOrdersWidgetClass() {
		return ordersWidgetClass;
	}

	public void setOrdersWidgetClass(Class<Widget> ordersWidgetClass) {
		this.ordersWidgetClass = ordersWidgetClass;
	}
	
	@Column(name = "BUILD_WIDGET_CLASS")
	@JsonIgnore
	public Class<Widget> getBuildWidgetClass() {
		return buildWidgetClass;
	}
	
	public void setBuildWidgetClass(Class<Widget> buildWidgetClass) {
		this.buildWidgetClass = buildWidgetClass;
	}
	
	@Column(name = "INCLUDE_IN_BUILD", columnDefinition = "TINYINT(1)")
	@JsonIgnore
	public Boolean getIncludeInBuild() {
		return includeInBuild;
	}

	public void setIncludeInBuild(Boolean includeInBuild) {
		this.includeInBuild = includeInBuild;
	}
	
	@Column(name = "ORDERS_SUBPANEL", columnDefinition = "TINYINT(1)")
	public Boolean getOrdersSubpanel() {
		return ordersSubpanel;
	}

	public void setOrdersSubpanel(Boolean ordersSubpanel) {
		this.ordersSubpanel = ordersSubpanel;
	}

	@Column(name = "INCLUDE_IN_ORDERS", columnDefinition = "TINYINT(1)")
	@JsonIgnore
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
	
	@Column(name = "INCLUDE_IN_PRODUCT_INFO", columnDefinition = "TINYINT(1)")
	@JsonIgnore
	public Boolean getIncludeInProductInfo() {
		return includeInProductInfo;
	}

	public void setIncludeInProductInfo(Boolean includeInProductInfo) {
		this.includeInProductInfo = includeInProductInfo;
	}
	
	@Column(name = "INCLUDE_IN_REPRINT", columnDefinition = "TINYINT(1)")
	public Boolean getIncludeInReprint() {
		return includeInReprint;
	}

	public void setIncludeInReprint(Boolean includeInReprint) {
		this.includeInReprint = includeInReprint;
	}

	@Column(name = "SORT_TYPE")
	@Enumerated(EnumType.STRING)
	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
	
	@Column(name = "PARAMS", columnDefinition="TEXT")
	@JsonIgnore
	public String getParamsAsString() {
		return paramsAsString;
	}
	public void setParamsAsString(String paramsAsString) {
		this.paramsAsString = paramsAsString;
	}
	
	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	@JsonIgnore
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "SORT_ORDER")
	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
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
	
	// Effective getters. Some of the properties of an TProductOptionType instance
	// can be overridden here so effective getters provide a way to quickly access an original or overridden value 

	@Transient
	public String getEffectiveDefaultValue() {
		if (this.getDefaultValue()!=null)
			return this.getDefaultValue();
		else
			return this.getProductOptionType().getDefaultValue();
	}
	
	@Transient
	public String getEffectiveCode() {
		if (this.getCode()!=null)
			return this.getCode();
		else
			return this.getProductOptionType().getCode();
	}
	
	@Transient
	@JsonIgnore
	public TResource getEffectiveLabel() {
		if (this.getLabel()!=null)
			return this.getLabel();
		return this.getProductOptionType().getResource();
	}
	
	@Transient
	public String getEffectiveDisplayLabel() {
		TResource r = getEffectiveLabel();
		if (r!=null)
			return r.getTranslatedValue();
		return null;
	}
	
	@Transient
	@JsonIgnore
	public TResource getEffectivePrompt() {
		if (this.getPrompt()!=null)
			return this.getPrompt();
		
		return this.getProductOptionType().getPrompt();
	}
	
	@Transient
	public String getUrl() {
		return UrlUtil.slug(getEffectiveDisplayLabel());
	}
	
	@Transient
	@JsonIgnore
	public Set<Validator> getEffectiveValidators() {
		if(getValidators() == null || getValidators().size() == 0) {
			return getProductOptionType().getValidators();
		}
		return getValidators();
	}
	
	@Transient
	@JsonSerialize(using = SimpleProductOptionGroupSerializer.class)
	public ProductOptionGroup getEffectiveGroup() {
		if (getGroup()==null)
			return getProductOptionType().getGroup();
		return getGroup();
	}
	
	@Transient
	public Class<Widget> getEffectiveOrdersWidgetClass() {
		if (getOrdersWidgetClass()==null)
			return getProductOptionType().getOrdersWidgetClass();
		return getOrdersWidgetClass();
	}
	
	@Transient
	@JsonIgnore
	public Class<Widget> getEffectiveBuildWidgetClass() {
		if (getBuildWidgetClass()==null)
			return getProductOptionType().getBuildWidgetClass();
		return getBuildWidgetClass();
	}
	
	@Transient
	public Boolean getEffectiveIncludeInBuild() {
		if (getIncludeInBuild()==null)
			return getProductOptionType().getIncludeInBuild();
		return getIncludeInBuild();
	}
	
	@Transient
	public Boolean getEffectiveIncludeInOrders() {
		if (getIncludeInOrders()==null)
			return getProductOptionType().getIncludeInOrders();
		return getIncludeInOrders();
	}
	
	@Transient 
	public Map<String,Object> getEffectiveParams() {
		if (getParamsAsString()!=null)
			return getParams();
		return getProductOptionType().getParams();
	}
	
	@Transient
	public SystemAttribute getSystemAttribute() {
		return getProductOptionType().getSystemAttribute();
	}
	
	@Transient
	public Boolean getIsRequired() {
		if (BooleanUtils.isTrue(getRequired()))
			return true;
		
		for(Validator v:getEffectiveValidators()) {
			if (v.gettValidator().getConcreteValidatorClass().isAssignableFrom(ProductOptionMandatoryValidator.class))
				return true;
		}
		return false;
	}
	
	
}
