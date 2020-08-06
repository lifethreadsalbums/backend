package com.poweredbypace.pace.domain.order;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Discount;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.TResource;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.util.SpringContextUtil;

@Entity
@Table(name = "O_LINE_ITEM")
public class LineItem extends BaseEntity {
	
	private static final long serialVersionUID = -3431741611996484434L;

	private String code;
	private TResource label;
	private Discount discount;
	private Integer quantity;
	private Money price;
	private Money subtotalPrice;
	private Money totalPrice;
	private ProductOptionValue productOptionValue;
	private ProductPrice productPrice;
	private Boolean labelIsExpression;
	private Integer order;
	
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	@JsonIgnore
	public TResource getLabel() {
		return label;
	}
	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@ManyToOne
	@JoinColumn(name = "DISCOUNT_ID")
	public Discount getDiscount() {
		return discount;
	}
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	
	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "PRICE_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "PRICE_AMOUNT")) ,
		})
	public Money getPrice() {
		return price;
	}
	public void setPrice(Money price) {
		this.price = price;
	}
	
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "SUBTOTAL_PRICE_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "SUBTOTAL_PRICE_AMOUNT")) ,
		})
	public Money getSubtotalPrice() {
		return subtotalPrice;
	}
	public void setSubtotalPrice(Money subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
	}
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name = "currency", column = @Column(name = "TOTAL_PRICE_CURRENCY")),
		@AttributeOverride(name = "amount", column = @Column(name = "TOTAL_PRICE_AMOUNT")) ,
		})
	public Money getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Money totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_PRICE_ID")
	@JsonIgnore
	public ProductPrice getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}
	
	@JsonIgnore
	@ManyToOne(optional=true)
	@JoinColumn(name = "PRODUCT_OPTION_VALUE_ID", nullable=true)
	public ProductOptionValue getProductOptionValue() {
		return productOptionValue;
	}
	public void setProductOptionValue(ProductOptionValue productOptionValue) {
		this.productOptionValue = productOptionValue;
	}
	
	@Column(name = "LABEL_IS_EXPRESSION", columnDefinition = "TINYINT(1)")
	public Boolean getLabelIsExpression() {
		return labelIsExpression;
	}
	public void setLabelIsExpression(Boolean labelIsExpression) {
		this.labelIsExpression = labelIsExpression;
	}
	
	@Column(name = "SORT_ORDER")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	@Transient
	public String getDisplayName()
	{
		if (productOptionValue!=null && productOptionValue.getValue()!=null) {
			if ( productOptionValue.getPrototypeProductOption().getLabel()!=null)
				return "<b>"+productOptionValue.getPrototypeProductOption().getLabel().getTranslatedValue() +
						": </b><span>" + productOptionValue.getValue().getProductOptionValue().getDisplayName() + "</span>";
			else
				return "<b>"+productOptionValue.getPrototypeProductOption().getProductOptionType().getDisplayName() + 
						": </b><span>" + productOptionValue.getValue().getProductOptionValue().getDisplayName() + "</span>";
		} else if (BooleanUtils.isTrue(labelIsExpression)) {
			try {
				ExpressionEvaluator eval = SpringContextUtil.getExpressionEvaluator();
				ProductContext context = getProductPrice().getProduct().getProductContext();
				if (context==null)
					context = new ProductContext(getProductPrice().getProduct());
				
				String result = eval.evaluate(context, label.getTranslatedValue(), String.class);
				return result;
			} catch(Exception ex) {
				return null;
			}
		} else if (label!=null)
			return label.getTranslatedValue();
		
		return getCode();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result
				+ ((totalPrice == null) ? 0 : totalPrice.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineItem other = (LineItem) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (totalPrice == null) {
			if (other.totalPrice != null)
				return false;
		} else if (!totalPrice.equals(other.totalPrice))
			return false;
		return true;
	}
	
	
}
