package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.domain.layout.ElementPosition;
import com.poweredbypace.pace.domain.layout.Foil;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.json.SimplePrototypeProductOptionValueSerializer;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "P_PROTOTYPE_PRODUCT_OPTION_VALUE")
@JsonIgnoreProperties({"id", "version"})
public class PrototypeProductOptionValue extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5886889872809084669L;

	private TProductOptionValue productOptionValue;
	private PrototypeProductOption prototypeProductOption;
	private PrototypeProductOptionValue parent;
	private List<PrototypeProductOptionValue> children;
	
	private String priceExpression;
	private String visibilityExpression;
	private LayoutSize layoutSize;
	private CoverType coverType;
	private ElementPosition elementPosition;
	private Foil foil;
	private String description;
	private String paramsAsString;
	
	@JoinColumn(name = "T_PRODUCT_OPTION_VALUE")
	@ManyToOne
	public TProductOptionValue getProductOptionValue() {
		return productOptionValue;
	}

	public void setProductOptionValue(TProductOptionValue tProductOptionValue) {
		this.productOptionValue = tProductOptionValue;
	}

	@JsonIgnore
	@Column(name = "PRICE_EXPRESSION", columnDefinition="TEXT")
	public String getPriceExpression() {
		return priceExpression;
	}

	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}
	
	@JoinColumn(name = "PROTOTYPE_PRODUCT_OPTION")
	@ManyToOne(fetch=FetchType.LAZY)
	//@JsonSerialize(using=SimplePrototypeProductOptionSerializer.class)
	@JsonIgnore
	public PrototypeProductOption getPrototypeProductOption() {
		return prototypeProductOption;
	}

	public void setPrototypeProductOption(
			PrototypeProductOption prototypeProductOption) {
		this.prototypeProductOption = prototypeProductOption;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT")
	@JsonSerialize(using=SimplePrototypeProductOptionValueSerializer.class)
	public PrototypeProductOptionValue getParent() {
		return parent;
	}

	public void setParent(PrototypeProductOptionValue parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent",fetch=FetchType.LAZY)
	@JsonIgnore
	public List<PrototypeProductOptionValue> getChildren() {
		return children;
	}

	public void setChildren(List<PrototypeProductOptionValue> children) {
		this.children = children;
	}

	@Column(name = "VISIBILITY_EXPRESSION", columnDefinition="TEXT")
	public String getVisibilityExpression() {
		return visibilityExpression;
	}

	public void setVisibilityExpression(String visibilityExpression) {
		this.visibilityExpression = visibilityExpression;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAYOUT_SIZE", nullable=true)
	public LayoutSize getLayoutSize() {
		return layoutSize;
	}

	public void setLayoutSize(LayoutSize layoutSize) {
		this.layoutSize = layoutSize;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COVER_TYPE", nullable=true)
	public CoverType getCoverType() {
		return coverType;
	}

	public void setCoverType(CoverType coverType) {
		this.coverType = coverType;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ELEMENT_POSITION_ID", nullable=true)
	public ElementPosition getElementPosition() {
		return elementPosition;
	}

	public void setElementPosition(ElementPosition elementPosition) {
		this.elementPosition = elementPosition;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FOIL_ID", nullable=true)
	public Foil getFoil() {
		return foil;
	}

	public void setFoil(Foil foil) {
		this.foil = foil;
	}
	
	@JsonIgnore
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Transient 
	public Map<String,Object> getEffectiveParams() {
		if (getParamsAsString()!=null)
			return getParams();
		return getProductOptionValue().getParams();
	}
	
	@Transient
	public String getCode() {
		return getProductOptionValue().getCode();
	}
	
	@Transient
	public String getDisplayName() {
		return getProductOptionValue().getDisplayName();
	}
	
}
