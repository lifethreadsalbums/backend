package com.poweredbypace.pace.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.TResource;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.OrderContext;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.util.SpringContextUtil;

@Entity
@Table(name = "O_ORDER_ADJUSTMENT")
public class OrderAdjustment extends BaseEntity {

	private static final long serialVersionUID = -5578340159182352999L;

	private Order order;
	private ProductPrice productPrice;
	private Money amount;
	private TResource label;
	private Boolean labelIsExpression;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	@JsonIgnore
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	
	public Money getAmount() {
		return amount;
	}
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	
	@ManyToOne
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	public TResource getLabel() {
		return label;
	}
	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@Column(name = "LABEL_IS_EXPRESSION", columnDefinition = "TINYINT(1)")
	public Boolean getLabelIsExpression() {
		return labelIsExpression;
	}
	public void setLabelIsExpression(Boolean labelIsExpression) {
		this.labelIsExpression = labelIsExpression;
	}
	
	@Transient
	public String getDisplayName()
	{
		if (labelIsExpression) {
			ExpressionEvaluator eval = SpringContextUtil.getExpressionEvaluator();
			ExpressionContext context = null;
			
			if (getProductPrice()!=null) {
				context = getProductPrice().getProduct().getProductContext();
				if (context==null)
					context = new ProductContext(getProductPrice().getProduct());
			} else {
				context = new OrderContext(getOrder());
			}
				
			String result = eval.evaluate(context, 
					label.getTranslatedValue(), String.class);
			return result;
		}
		else if (label!=null)
			return label.getTranslatedValue();
		
		return null;
	}
}
