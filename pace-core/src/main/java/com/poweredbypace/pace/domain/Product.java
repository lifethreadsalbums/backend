package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.json.ProductDeserializer;
import com.poweredbypace.pace.json.ProductSerializer;

@Entity
@Table(name = "P_PRODUCT")
@SuppressWarnings("rawtypes")
@BatchSize(size=100)
@JsonSerialize(using=ProductSerializer.class)
@JsonDeserialize(using=ProductDeserializer.class)
public class Product extends BaseEntity implements Serializable {
	
	public enum SystemAttribute {
		Name,
		Quantity,
		Rush,
		StudioSample,
		PageCount,
		ProductPrototype,
		DateCreated,
		CustomLogo,
		ProductCategory,
		ReprintPages,
		UserNotes,
		HoldReason,
		TrackingId,
		ShippingDate,
		DeliveryDate
	}
	
	public enum ProductState {
		New,
		OnHold,
		Preflight,
		Cancelled,
		Printing,
		Printed,
		Bindery,
		ReadyToShip,
		Shipped,
		Completed,
	}

	private static final long serialVersionUID = 2563724229044016122L;

	private Set<ProductOption> productOptions = new HashSet<ProductOption>();
	
	private PrototypeProduct prototypeProduct;
	private User user;
	private List<Product> children = new ArrayList<Product>();
	private List<ProductPrice> productPrices = new ArrayList<ProductPrice>();
	private OrderItem orderItem;
	private Batch batch;
	private ProductState state = ProductState.New;
	private Money subtotal;
	private Money total;
	private Boolean isFavourite = false;
	private Boolean isCoverBuilderWizardEnabled = false;
	private Boolean linkLayout = true;
	private Boolean onHold = false;
	private Store store;
	private String productNumber;
	
	//properties specific to DesignableProduct only
	private Product parent;
	private Product original;
	private Layout layout;
	private Layout coverLayout;
	private List<Attachment> attachments = new ArrayList<Attachment>();
	private Boolean isReprint = false;
	
	private Boolean skipLowResCheck = false;
	private ProductContext productContext;
	private Integer childIndex;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	@Transient
	public String getName() {
		ProductOptionString option = (ProductOptionString) getSystemAttribute(SystemAttribute.Name);
		return option.getValue();
	}
	public void setName(String name) {
		ProductOptionString option = (ProductOptionString) getSystemAttribute(SystemAttribute.Name);
		option.setValue(name);
	}
	
	@Column(name="IS_CB_WIZARD_ENABLED", columnDefinition = "TINYINT(1)")
	public Boolean getIsCoverBuilderWizardEnabled() {
		return isCoverBuilderWizardEnabled;
	}
	public void setIsCoverBuilderWizardEnabled(Boolean isCoverBuilderWizardEnabled) {
		this.isCoverBuilderWizardEnabled = isCoverBuilderWizardEnabled;
	}
	
	//@OrderBy("id")
	@BatchSize(size=100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", 
		cascade={CascadeType.ALL}, orphanRemoval = true)
	public Set<ProductOption> getProductOptions() {
		return productOptions;
	}
	public void setProductOptions(Set<ProductOption> productOptions) {
		this.productOptions = productOptions;
	}

	@JoinColumn(name = "PROTOTYPE_PRODUCT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public PrototypeProduct getPrototypeProduct() {
		return prototypeProduct;
	}
	public void setPrototypeProduct(PrototypeProduct prototypeProduct) {
		this.prototypeProduct = prototypeProduct;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "IS_FAVOURITE", columnDefinition = "TINYINT(1)")
	public Boolean getIsFavourite() {
		return isFavourite;
	}
	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
	@Transient
	public Boolean getIsDuplicate() {
		return getParent()!=null;
	}
	
	@JoinColumn(name = "LAYOUT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@JoinColumn(name = "COVER_LAYOUT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Layout getCoverLayout() {
		return coverLayout;
	}
	public void setCoverLayout(Layout coverLayout) {
		this.coverLayout = coverLayout;
	}
	
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Product getParent() {
		return parent;
	}
	public void setParent(Product parentProduct) {
		this.parent = parentProduct;
	}
	
	@JoinColumn(name = "ORIGINAL_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Product getOriginal() {
		return original;
	}
	public void setOriginal(Product original) {
		this.original = original;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@OrderBy("childIndex")
	public List<Product> getChildren() {
		return children;
	}
	public void setChildren(List<Product> children) {
		this.children = children;
	}
	
	@Transient
	public Integer getQuantity() {
		ProductOptionInteger qty = (ProductOptionInteger) getSystemAttribute(SystemAttribute.Quantity);
		return qty.getValue();
	}

	public void setQuantity(Integer quantity) {
		ProductOptionInteger qty = (ProductOptionInteger) getSystemAttribute(SystemAttribute.Quantity);
		qty.setValue(quantity);
	}
	
	@OneToMany(fetch = FetchType.LAZY, 
		mappedBy = "product", 
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	public List<ProductPrice> getProductPrices() {
		return productPrices;
	}
	public void setProductPrices(List<ProductPrice> productPrices) {
		this.productPrices = productPrices;
	}
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "product")
	public OrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BATCH_ID", nullable = true)
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	
	/**
	 * Returns the price of the product without duplicates
	 * @return price
	 */
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SUBTOTAL_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SUBTOTAL_AMOUNT")) ,
		})
	public Money getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Money subtotal) {
		this.subtotal = subtotal;
	}
	
	/**
	 * Returns the total price of the product and its duplicates
	 * @return price
	 */
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "TOTAL_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "TOTAL_AMOUNT")) ,
		})
	public Money getTotal() {
		return total;
	}
	public void setTotal(Money total) {
		this.total = total;
	}
	
	@Column(name="STATE")
	@Enumerated(EnumType.STRING)
	public ProductState getState() {
		return state;
	}
	public void setState(ProductState state) {
		this.state = state;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	@Column(name="LINK_LAYOUT", columnDefinition = "TINYINT(1)")
	public Boolean getLinkLayout() {
		return linkLayout;
	}
	public void setLinkLayout(Boolean linkLayout) {
		this.linkLayout = linkLayout;
	}
	
	@Column(name="ON_HOLD", columnDefinition = "TINYINT(1)")
	public Boolean getOnHold() {
		return onHold;
	}
	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}
	
	@Column(name="PRODUCT_NUMBER")
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
	@Column(name="IS_REPRINT", columnDefinition = "TINYINT(1)")
	public Boolean getIsReprint() {
		return isReprint;
	}
	public void setIsReprint(Boolean isReprint) {
		this.isReprint = isReprint;
	}
	
	@Column(name="CHILD_INDEX")
	public Integer getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(Integer childIndex) {
		this.childIndex = childIndex;
	}
	
	@Transient
	@JsonIgnore
	public List<Product> getProductAndChildren() {
		List<Product> products = new ArrayList<Product>();
		products.add(this);
		products.addAll(getChildren());
		return products;
	}
	
	@Transient
	public Boolean getInCart() {
		if (getOrderItem()!=null && getOrderItem().getOrder().getState()==OrderState.Pending)
			return true;
		
		return false;
	}
	
	@Transient
	public OrderState getOrderState() {
		if (getOrderItem()!=null)
			return getOrderItem().getOrder().getState();
		return null;
	}
	
	@Transient
	@JsonIgnore
	public LayoutSize getLayoutSize() {
		
		for(ProductOption<?> productOption:getProductOptions()) {
			
			if(productOption.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) productOption;
				PrototypeProductOptionValue value = productOptionValue.getValue();
				if (value!=null && value.getLayoutSize()!=null)
					return value.getLayoutSize();
			} 
		}
		return null;
	}
	
	@Transient
	@JsonIgnore
	public CoverType getCoverType() {
		
		for(ProductOption<?> productOption:getProductOptions()) {
			
			if(productOption.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) productOption;
				PrototypeProductOptionValue value = productOptionValue.getValue();
				if (value!=null && value.getCoverType()!=null)
					return value.getCoverType();
			} 
		}
		return null;
	}
	
	@Transient
	public ProductOption<?> getProductOptionByCode(String code) {
		assert(code!=null);
		for(ProductOption<?> option:getProductOptions()) {
			if (code.equals(option.getPrototypeProductOption().getEffectiveCode()))
				return option;
		}
		return null;
	}
	
	@Transient
	public <T> T getProductOptionValue(String code, Class<T> clazz) {
		ProductOption<?> po = getProductOptionByCode(code);
		if (po==null) return null;
		Object value = po.getValue();
		Hibernate.initialize(value);
		if (value instanceof HibernateProxy) { 
			value = ((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
		}
		return clazz.cast( value );
	}
	
	@Transient
	public void setProductOptionCode(String optionCode, String valueCode) {
		ProductOption<?> po = getProductOptionByCode(optionCode);
		if (po == null) {
			PrototypeProductOption prototypeProductOption = this.getPrototypeProduct().getOptionByCode(optionCode);
			if (prototypeProductOption != null) {
				try {
					po = prototypeProductOption.getProductOptionType().getProductOptionClass().newInstance();
					po.setProduct(this);
					po.setPrototypeProductOption(prototypeProductOption);
					this.getProductOptions().add(po);
				} catch (Exception e) {
					// log.error("", e);
				}
			}
		}
		if (po.getClass().isAssignableFrom(ProductOptionValue.class)) {
			ProductOptionValue productOptionValue = (ProductOptionValue) po;
			for (PrototypeProductOptionValue val : po.getPrototypeProductOption().getPrototypeProductOptionValues()) {
				if (val.getProductOptionValue().getCode().equals(valueCode)) {
					productOptionValue.setValue(val);
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public <T> void setProductOptionValue(String optionCode, T value) {
		ProductOption<?> po = getProductOptionByCode(optionCode);
		if (po == null) {
			PrototypeProductOption prototypeProductOption = this.getPrototypeProduct().getOptionByCode(optionCode);
			if (prototypeProductOption != null) {
				try {
					po = prototypeProductOption.getProductOptionType().getProductOptionClass().newInstance();
					po.setProduct(this);
					po.setPrototypeProductOption(prototypeProductOption);
					this.getProductOptions().add(po);
				} catch (Exception e) {
					// log.error("", e);
				}
			}
		}
		if (po!=null) {
			ProductOption<T> o = (ProductOption<T>) po;
			o.setValue(value);
		}
	}
	
	@Transient
	public String getProductOptionCode(String code) {
		ProductOption<?> po = this.getProductOptionByCode(code);
		if (po!=null) {
			if (ProductOptionValue.class.isAssignableFrom(po.getClass()) && po.getValue()!=null ) {
				return ((ProductOptionValue) po).getPrototypeProductOptionValue().getCode();
			}
			return po.getDisplayValue();
		}
		return null;
	}
	
	@Transient
	public String getProductOptionDisplayValue(String code) {
		ProductOption<?> po = this.getProductOptionByCode(code);
		return po!=null ? po.getDisplayValue() : null;
	}
	
	@Transient
	public String getProductType() {
		ProductOption<?> po = getSystemAttribute(SystemAttribute.ProductPrototype);
		return po!=null ? po.getDisplayValue() : null;
	}
	
	@Transient
	public String getUserNotes() {
		ProductOption<?> po = getSystemAttribute(SystemAttribute.UserNotes);
		return po!=null ? po.getDisplayValue() : null;
	}
	
	@Transient
	public void setUserNotes(String notes) {
		ProductOption<?> po = getSystemAttribute(SystemAttribute.UserNotes);
		if (po!=null) {
			ProductOptionString pos = (ProductOptionString) po;
			pos.setValue(notes);
		}
	}
	
	@Transient
	public String getProductCategory() {
		ProductOption<?> po = getSystemAttribute(SystemAttribute.ProductCategory);
		if (po!=null) {
			//TODO: handle plural and singular names better
			String val = po.getDisplayValue();
			Integer qty = this.getQuantity();
			if (qty!=null && qty.intValue()>1) {
				return val;
			} else {
				return val.replaceFirst("s$", "");
			}
		}
		return null;
	}
	
	@Transient
	public String getHighResPdfUrl() {
		Attachment a = getAttachment(AttachmentType.HiResPdf);
		return a!=null ? getStore().getStorageUrl() + a.getUrl() : null;
	}
	
	@Transient
	public String getBinderyFormUrl() {
		Attachment a = getAttachment(AttachmentType.BinderyForm);
		return a!=null ? getStore().getStorageUrl() + a.getUrl() : null;
	}
	
	@Transient
	public String getHighResJpegUrl() {
		Attachment a = getAttachment(AttachmentType.HiResJpeg);
		return a!=null ? getStore().getStorageUrl() + a.getUrl() : null;
	}
	
	@Transient
	public String getLowResPdfUrl() {
		Attachment a = getAttachment(AttachmentType.LowResPdf);
		if (a!=null) {
			Random generator = new Random(); 
			int v = generator.nextInt(1000000) + 1;
			return getStore().getStorageUrl() + a.getUrl() + "?v=" + v;
		}
		return null;
	}
	
	@Transient
	public boolean isReprint() {
		return BooleanUtils.isTrue(isReprint);
	}
	
	@Transient
	public boolean isDuplicate() {
		return getParent()!=null;
	}
	
	@Transient
	public String getReprintPages() {
		ProductOptionString value = (ProductOptionString) getSystemAttribute(SystemAttribute.ReprintPages);
		return value!=null ? value.getValue() : null;
	}
	
	@Transient
	public ProductOption<?> getSystemAttribute(SystemAttribute attr) {
		for(ProductOption<?> option:getProductOptions()) {
			if (option.getPrototypeProductOption()
					.getProductOptionType().getSystemAttribute()==attr)
				return option;
		}
		return null;
	}
	
	@Transient
	public Attachment getAttachment(AttachmentType type) {
		for(Attachment a:getAttachments()) {
			if (a.getType()==type)
				return a;
		}
		return null;
	}
	
	@Transient
	public String getAttachmentUrl(String type) {
		Attachment a = getAttachment(AttachmentType.valueOf(type));
		return a!=null ? getStore().getStorageUrl() + a.getUrl() : null;
	}
	
	@Transient
	public String getAttachmentRelativeUrl(String type) {
		Attachment a = getAttachment(AttachmentType.valueOf(type));
		return a!=null ? a.getUrl() : null;
	}
	
	@Transient
	public Boolean getStudioSample() {
		ProductOptionBoolean option = (ProductOptionBoolean) getSystemAttribute(SystemAttribute.StudioSample);
		if (option!=null)
			return option.getValue();
		else
			return false;
	}
	public void setStudioSample(Boolean isStudioSample) {
		ProductOptionBoolean option = (ProductOptionBoolean) getSystemAttribute(SystemAttribute.StudioSample);
		option.setValue(isStudioSample);
	}
	
	@Transient
	public String getTrackingId() {
		ProductOptionString option = (ProductOptionString) getSystemAttribute(SystemAttribute.TrackingId);
		if (option!=null)
			return option.getValue();
		else
			return null;
	}
	
	@Transient
	public Boolean getRush() {
		ProductOptionBoolean option = (ProductOptionBoolean) getSystemAttribute(SystemAttribute.Rush);
		if (option!=null)
			return option.getValue();
		else
			return false;
	}
	public void setRush(Boolean value) {
		ProductOptionBoolean option = (ProductOptionBoolean) getSystemAttribute(SystemAttribute.Rush);
		option.setValue(value);
	}
	
	@Transient
	public Date getDateCreated() {
		ProductOptionDate option = (ProductOptionDate) getSystemAttribute(SystemAttribute.DateCreated);
		return option.getValue();
	}
	public void setDateCreated(Date dateCreated) {
		ProductOptionDate option = (ProductOptionDate) getSystemAttribute(SystemAttribute.DateCreated);
		option.setValue(dateCreated);
	}
	
	@Transient
	public Integer getPageCount() {
		ProductOptionInteger option = (ProductOptionInteger) getSystemAttribute(SystemAttribute.PageCount);
		if (option.getValue()!=null)
			return option.getValue();
		else
			return 0;
	}
	public void setPageCount(Integer value) {
		ProductOptionInteger option = (ProductOptionInteger) getSystemAttribute(SystemAttribute.PageCount);
		option.setValue(value);
	}
	
	@Transient
	public Boolean getSkipLowResCheck() {
		return skipLowResCheck;
	}
	public void setSkipLowResCheck(Boolean skipLowResCheck) {
		this.skipLowResCheck = skipLowResCheck;
	}
	
	@Transient
	@JsonIgnore
	public ProductContext getProductContext() {
		return productContext;
	}
	public void setProductContext(ProductContext productContext) {
		this.productContext = productContext;
	}
	
}