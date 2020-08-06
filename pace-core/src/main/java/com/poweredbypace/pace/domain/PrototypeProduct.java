package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductPrototypeContext;
import com.poweredbypace.pace.util.SpringContextUtil;

@Entity
@Table(name = "P_PROTOTYPE_PRODUCT")
public class PrototypeProduct extends BaseEntity implements Serializable {
	
	final public static String REPRINT = "reprint"; 
	
	public enum ProductPageType {
		PageBased,
		SpreadBased
	}
	
	public enum FirstPageType {
		LeftPageStart,
		RightPageStart
	}
	
	public enum ProductType {
		DesignableProduct,
		SinglePrintProduct,
		NondesignableProduct
	}

	private static final long serialVersionUID = -432538895709498444L;

	private String code;
	private String tag;
	private Set<PrototypeProductOption> prototypeProductOptions;
	private Set<Store> stores;
	private Boolean isDefault;
	private ProductPageType productPageType;
	private ProductType productType;
	private FirstPageType firstPageType;
	private Boolean freeShipping;
	private Boolean allowDuplicates;
	private TResource singularLabel;
	private TResource pluralLabel;
	private TResource duplicateLabel;
	private String coverBuilderMask;
	private Integer minSpreads;
	
	
	@Column(name = "PRODUCT_PAGE_TYPE")
	@Enumerated(EnumType.STRING)
	public ProductPageType getProductPageType() {
		return productPageType;
	}
	public void setProductPageType(ProductPageType productPageType) {
		this.productPageType = productPageType;
	}
	
	@Column(name = "PRODUCT_TYPE")
	@Enumerated(EnumType.STRING)
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "TAG")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prototypeProduct")
	public Set<PrototypeProductOption> getPrototypeProductOptions() {
		return prototypeProductOptions;
	}
	
	public void setPrototypeProductOptions(
			Set<PrototypeProductOption> prototypeProductOptions) {
		this.prototypeProductOptions = prototypeProductOptions;
	}
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "P_STORE_PROTOTYPE_PRODUCT", joinColumns = { 
			@JoinColumn(name = "PROTOTYPE_PRODUCT_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "STORE_ID", 
					nullable = false, updatable = false) })
	public Set<Store> getStores() {
		return stores;
	}
	public void setStores(Set<Store> stores) {
		this.stores = stores;
	}
	
	@Column(name="IS_DEFAULT", columnDefinition = "TINYINT(1)")
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Column(name = "FIRST_PAGE_TYPE")
	@Enumerated(EnumType.STRING)
	public FirstPageType getFirstPageType() {
		return firstPageType;
	}
	public void setFirstPageType(FirstPageType firstPageType) {
		this.firstPageType = firstPageType;
	}
	
	
	@Column(name = "FREE_SHIPPING", columnDefinition = "TINYINT(1)")
	public Boolean getFreeShipping() {
		return freeShipping;
	}
	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "SINGULAR_LABEL_ID")
	@JsonIgnore
	public TResource getSingularLabel() {
		return singularLabel;
	}
	public void setSingularLabel(TResource singularLabel) {
		this.singularLabel = singularLabel;
	}
	
	@ManyToOne
	@JoinColumn(name = "PLURAL_LABEL_ID")
	@JsonIgnore
	public TResource getPluralLabel() {
		return pluralLabel;
	}
	public void setPluralLabel(TResource pluralLabel) {
		this.pluralLabel = pluralLabel;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "DUPLICATE_LABEL_ID")
	@JsonIgnore
	public TResource getDuplicateLabel() {
		return duplicateLabel;
	}
	public void setDuplicateLabel(TResource duplicateLabel) {
		this.duplicateLabel = duplicateLabel;
	}
	
	@Column(name = "CB_MASK")
	public String getCoverBuilderMask() {
		return coverBuilderMask;
	}
	public void setCoverBuilderMask(String coverBuilderMask) {
		this.coverBuilderMask = coverBuilderMask;
	}
	
	@Column(name = "MIN_SPREADS")
	public Integer getMinSpreads() {
		return minSpreads;
	}
	public void setMinSpreads(Integer minSpreads) {
		this.minSpreads = minSpreads;
	}
	
	@Column(name = "ALLOW_DUPLICATES", columnDefinition = "TINYINT(1)")
	public Boolean getAllowDuplicates() {
		return allowDuplicates;
	}
	public void setAllowDuplicates(Boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}
	
	private String getDisplayLabel(TResource label) {
		try {
			ExpressionEvaluator eval = SpringContextUtil.getExpressionEvaluator();
			ProductPrototypeContext  context = new ProductPrototypeContext(this);
			
			String result = eval.evaluate(context, label.getTranslatedValue(), String.class);
			return result;
		} catch(Exception ex) {
			if (label!=null)
				return label.getTranslatedValue();
		}
		
		return null;
	}
	
	@Transient
	public String getSingularDisplayName()
	{
		return getDisplayLabel(getSingularLabel());
	}
	
	@Transient
	public String getPluralDisplayName()
	{
		return getDisplayLabel(getPluralLabel());
	}
	
	@Transient
	public String getDuplicateDisplayName()
	{
		return getDisplayLabel(getDuplicateLabel());
	}
	
	@Transient
	public PrototypeProductOption getOptionBySystemAttribute(SystemAttribute attr) {
		for(PrototypeProductOption o:this.getPrototypeProductOptions()) {
			if (o.getProductOptionType().getSystemAttribute()==attr)
				return o;
		}
		return null;
	}
	
	@Transient
	public PrototypeProductOption getOptionByCode(String code) {
		for(PrototypeProductOption o:this.getPrototypeProductOptions()) {
			if (o.getEffectiveCode().equals(code))
				return o;
		}
		return null;
	}
	
	@Transient
	public boolean isReprint() {
		return REPRINT.equals(getCode());
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getVersion())
			.append(getCode())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if( o == null)
		    return false;
		
		if (getClass() != o.getClass())
			return false;
		  
		PrototypeProduct that = (PrototypeProduct)o;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getId(), that.getId());
		b.append(getVersion(), that.getVersion());
		b.append(getCode(), that.getCode());
		
		return b.isEquals();
	}
	
	
}