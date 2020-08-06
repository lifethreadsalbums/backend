package com.poweredbypace.pace.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Discount;
import com.poweredbypace.pace.domain.DiscountRule;
import com.poweredbypace.pace.domain.DiscountRule.CouponType;
import com.poweredbypace.pace.domain.DiscountRule.DiscountRuleType;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.PriceRule;
import com.poweredbypace.pace.domain.PriceRule.PriceRuleType;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.TaxRate;
import com.poweredbypace.pace.domain.order.LineItem;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderAdjustment;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.order.OrderTax;
import com.poweredbypace.pace.exception.CouponNotValidException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.OrderContext;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.repository.DiscountRuleRepository;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.repository.PriceRuleRepository;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.TaxService;

/***
 * Service for calculating product prices
 *
 */
@Service
public class PricingServiceImpl implements PricingService {

	private final Log log = LogFactory.getLog(PricingServiceImpl.class);
	
	private List<PriceRule> priceRules;
	private List<DiscountRule> discountRules;
	
	private Object lock = new Object();
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private DiscountRuleRepository discountRuleService;
	
	@Autowired
	private PriceRuleRepository priceRuleService;
	
	@Autowired
	private TaxService taxService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CurrencyManager currencyManager;
	
	@Autowired
	@PersistenceContext(unitName="paceUnit")
	private EntityManager entityManager;
	
	public void clearCache() {
		synchronized(lock) {
			discountRules = null;
			priceRules = null;
		}
		initRules();
	}
	
	private void initRules() {
		if (discountRules!=null) return;
		
		synchronized(lock) {
			discountRules = discountRuleService.findAll();
			priceRules = priceRuleService.findAll();
			for(DiscountRule rule:discountRules) {
				rule.getLineItemCodes().size();
				if (rule.getLabel()!=null)
					rule.getLabel().getTranslations().size();
				entityManager.detach(rule);
			}
			for(PriceRule rule:priceRules) {
				if (rule.getLabel()!=null)
					rule.getLabel().getTranslations().size();
				rule.getGroup().size();
				entityManager.detach(rule);
			}
		}
	}
	
	/**
	 * Check if the coupon code is valid
	 * @param order 		the {@link Order} instance
	 * @param couponCode	the coupon code
	 * @throws CouponNotValidException
	 */
	public void checkCoupon(Order order, String couponCode) {
		
		long couponUses = orderRepository.countByCouponCodeAndUserAndIdNot(couponCode, 
				order.getUser(), order.getId());
		if (couponUses>0) {
			List<Order> orders = orderRepository.findByCouponCodeAndUserAndIdNot(couponCode, 
					order.getUser(), order.getId());
			if (orders.size()>0) {
				Order o = orders.get(0);
				String message = "The coupon \"" + couponCode + "\" has previously been used.";
				if (ObjectUtils.equals(o.getUser().getId(), order.getUser().getId())) {
					SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy");
					String date = sdf.format(o.getDateCreated());
					
					//The coupon {coupon name} has previously been used on {date used} with Order {OrderID}.
					message = "The coupon \"" + couponCode + "\" has previously been used on "
							+ date + " with Order " + o.getOrderNumber() + ".";
				}
				throw new CouponNotValidException(message);
			}
		} else {
			int numDiscounts = 0;
			for(DiscountRule rule: discountRules) {
	  			if (rule.getCouponType()==CouponType.SpecificCoupon && 
	  				StringUtils.equals(rule.getCouponCode(), couponCode))
	  				numDiscounts++;
			}
			if (numDiscounts==0) {
				throw new CouponNotValidException("The coupon \"" + couponCode + "\" is not valid.");
			}
		}
		
	}
	
	/**
	 * Calculates price for the given product
	 * @param product	{@link Product} instance
	 */
	public void executePricing(Product product) {
		long startTime = System.currentTimeMillis();
		Currency currency = currencyManager.getCurrency(product.getUser());
		initRules();
		executePricing(product, null, currency);
		calculateSubtotal(product, currency);
		log.debug("executePricing(Product): " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	/**
	 * Calculates price for the given order
	 * @param order	{@link Order} instance
	 */
	@Override
	public void executePricing(Order order) {
		long startTime = System.currentTimeMillis();
		
		Currency currency = currencyManager.getCurrency(order.getUser());
		initRules();

		int idx = 0;
		for(OrderItem oi:order.getOrderItems()) {
			oi.setListOrder(idx++);
		}
		
		order.getTaxes().clear();
		order.getOrderAdjustments().clear();
		
		OrderContext orderContext = new OrderContext(order);
		for(OrderItem orderItem:order.getOrderItems()) {
			executePricing(orderItem.getProduct(), order, currency);
			calculateSubtotal(orderItem.getProduct(), currency);
		}
		
		calculateSubtotal(order, currency);
		applyCartPriceRules(order, orderContext, currency);
		calculateAdjustments(order, currency);
		applyTaxes(order, currency);
		calculateGrandTotal(order, currency);
		
		log.debug("executePricing(Order): " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	private void executePricing(Product parentProduct, Order order, Currency currency) {
		int duplicateIndex = 0;
		boolean firstNotSample = false;
		for(Product product:parentProduct.getProductAndChildren()) {
			
			if (product.isReprint()) {
				//reset price for reprint
				product.getProductPrices().clear();
				product.getProductPrices().add(new ProductPrice());
				continue;
			}
			
			ProductContext productContext = new ProductContext(product);
			productContext.setDuplicateIndex(duplicateIndex++);
			
			if (!firstNotSample && BooleanUtils.isNotTrue(product.getStudioSample())) {
				firstNotSample = true;
				productContext.setFirstNotSample(true);
			}
			
			Integer qty = product.getQuantity();
			int quantity = qty!=null ? qty.intValue() : 0;
			
			if (product.getPrototypeProduct().getProductType()!=ProductType.DesignableProduct) {
				quantity = 1;
			}
			
			for(int i=0;i<quantity;i++) {
				if (product.getParent()==null && i>0) {
					productContext.setParent(product);
				}
				ProductPrice price = null;
				if (i<product.getProductPrices().size()) {
					price = product.getProductPrices().get(i);
				} else {
					price = new ProductPrice();
					price.setProduct(product);
					product.getProductPrices().add(price);
				}
				
				price.getOrderSubItemAdjustments().clear();
				price.getLineItems().clear();
				
				processProductOptions(price, productContext, order, currency);
				applyLineItemPriceRules(price, productContext, order, currency);
				applyLineItemGroupingRules(price, productContext, order, currency);
				
				if (i==0) {
					price.setGroupId(i+"");
				} else {
					ProductPrice prevPrice = product.getProductPrices().get(i - 1);
					if (productPriceEquals(price, prevPrice)) {
						price.setGroupId(prevPrice.getGroupId());
					} else {
						price.setGroupId(i+"");
					}
				}
			}
			if (quantity<product.getProductPrices().size()) {
				int n = product.getProductPrices().size() - quantity;
				for(int i=0;i<n;i++) {
					product.getProductPrices().remove(product.getProductPrices().size() - 1);
				}
			}
			//merge product prices
			Map<String, List<ProductPrice>> groups = new HashMap<String, List<ProductPrice>>();
			
			for(ProductPrice price:product.getProductPrices()) {
				List<ProductPrice> group = groups.get(price.getGroupId());
				if (group==null) {
					group = new ArrayList<ProductPrice>();
					groups.put(price.getGroupId(), group);
				}
				group.add(price);
			}
			List<ProductPrice> prices = new ArrayList<ProductPrice>();
			for(String key:groups.keySet()) {
				List<ProductPrice> group = groups.get(key);
				ProductPrice firstPrice = group.get(0);
				for(int i = 1;i<group.size();i++) {
					ProductPrice price = group.get(i);
					for(int j=0;j<firstPrice.getLineItems().size();j++) {
						LineItem la = firstPrice.getLineItems().get(j);
						LineItem lb = price.getLineItems().get(j);
						if (lb.getQuantity()!=null && la.getQuantity()!=null) { 
							la.setQuantity(la.getQuantity() + lb.getQuantity());
						}
						if (la.getSubtotalPrice()!=null && lb.getSubtotalPrice()!=null) {
							la.getSubtotalPrice().setAmount(la.getSubtotalPrice().getAmount().add(lb.getSubtotalPrice().getAmount()));
						}
						if (la.getTotalPrice()!=null && lb.getTotalPrice()!=null) {
							la.getTotalPrice().setAmount(la.getTotalPrice().getAmount().add(lb.getTotalPrice().getAmount()));
						}
					}
				}
				prices.add(firstPrice);
			}
			product.getProductPrices().clear();
			product.getProductPrices().addAll(prices);
		}
	}
	
	private boolean lineItemEquals(LineItem a, LineItem b) {
		return ObjectUtils.equals(a.getCode(), b.getCode()) &&
			ObjectUtils.equals(a.getDiscount(), b.getDiscount()) &&
			ObjectUtils.equals(a.getLabel(), b.getLabel()) &&
			ObjectUtils.equals(a.getLabelIsExpression(), b.getLabelIsExpression()) &&
			ObjectUtils.equals(a.getPrice(), b.getPrice()) &&
			ObjectUtils.equals(a.getQuantity(), b.getQuantity()) &&
			ObjectUtils.equals(a.getProductOptionValue(), b.getProductOptionValue());
	}
	
	private boolean productPriceEquals(ProductPrice a, ProductPrice b) {
		if (a.getLineItems().size()==b.getLineItems().size()) {
			int n = a.getLineItems().size();
			for(int i=0;i<n;i++) {
				if (!lineItemEquals(a.getLineItems().get(i), b.getLineItems().get(i))) 
					return false;
			}
			return true;
		}
		return false;
	}
	
	private Discount getLineItemDiscount(ProductContext productContext,
			String optionCode, Order order) {

		String couponCode = order!=null ? order.getCouponCode() : null;
  		for(DiscountRule rule: discountRules) {
  			
  			if (rule.getType()!=DiscountRuleType.LineItemDiscount)
  				continue;
  			
  			if (rule.getCouponType()==CouponType.SpecificCoupon && 
  				!StringUtils.equals(rule.getCouponCode(), couponCode))
  				continue;
  			
  			if (rule.getLineItemCodes().indexOf(optionCode)>=0) {
  			
	  			try {
		  			Boolean condition = expressionEvaluator.evaluate(productContext, 
							rule.getConditionExpression(), 
							Boolean.class);
		  			
		  			if (BooleanUtils.isTrue(condition)) {
		  				return rule.getDiscount();
		  			}
	  			} catch(Exception e) {
	  				logError(rule, e);
	  			}
	  			
  			}
  		}

  		return null;
  	}
	
	private List<DiscountRule> getOrderDiscountRules(Order order, OrderContext orderContext) {

		List<DiscountRule> result = new ArrayList<DiscountRule>();
  		for(DiscountRule rule: discountRules) {
  			
  			if (rule.getType()!=DiscountRuleType.OrderDiscount)
  				continue;
  			
  			if (rule.getCouponType()==CouponType.SpecificCoupon) {
  				if (!isCouponValid(rule, order)) continue;
  			}
  			
  			try {
	  			Boolean condition = expressionEvaluator.evaluate(orderContext, 
						rule.getConditionExpression(), 
						Boolean.class);
	  			
	  			if (BooleanUtils.isTrue(condition)) {
	  				result.add(rule);
	  			}
  			} catch(Exception e) {
  				logError(rule, e);
  			}
  		}

  		return result;
  	}
	
	@SuppressWarnings("rawtypes")
	private void processProductOptions(ProductPrice productPrice, ProductContext productContext, Order order, Currency currency) {
		
		Product product = productPrice.getProduct();
		Integer qty = product.getQuantity();
		if (qty==null) return;
		
		int index = 10000;
		for(ProductOption productOption:product.getProductOptions())
		{
			if(productOption.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) productOption;
				
				if (BooleanUtils.isTrue(productOptionValue.getPrototypeProductOption().getIncludeAsLineItem()) && 
					productOptionValue.getValue()!=null)
				{	
					
					String priceExpression = productOptionValue.getValue().getPriceExpression();
					if (priceExpression!=null) {
						Money price = new Money( 
							expressionEvaluator.evaluate(productContext, priceExpression, Float.class),
							currency.getCurrencyCode()
						);
					
						String optionCode = productOptionValue.getPrototypeProductOption().getProductOptionType().getCode();
						if (productOptionValue.getPrototypeProductOption().getCode()!=null) 
							optionCode = productOptionValue.getPrototypeProductOption().getCode();
						
						Discount discount = getLineItemDiscount(productContext,
								optionCode, 
								order);
						
						BigDecimal total = price.getAmount()
								.multiply( new BigDecimal(product.getQuantity()) );
						
						if (discount!=null)
							total = total.subtract( discount.getDiscountValue(total) );
						
						//add line item
						LineItem lineItem = new LineItem();
						lineItem.setOrder(index);
						lineItem.setProductPrice(productPrice);
						lineItem.setCode(optionCode);
						lineItem.setProductOptionValue(productOptionValue);
						lineItem.setPrice(price);
						lineItem.setDiscount(discount);
						lineItem.setTotalPrice(new Money(total, currency.getCurrencyCode()));
						lineItem.setQuantity(product.getQuantity());
						productPrice.getLineItems().add(lineItem);
					}
					
					index++;
				}
			}
		}
		
	}
	
	private void applyLineItemPriceRules(ProductPrice productPrice, ProductContext productContext, Order order, Currency currency) {
		
		for(PriceRule rule:priceRules)
		{
			if (PriceRuleType.LineItemRule != rule.getType())
				continue;
			
			try {
				Boolean condition = expressionEvaluator.evaluate(productContext, 
						rule.getConditionExpression(), 
						Boolean.class);
				if (BooleanUtils.isTrue(condition)) {
					
					if (BooleanUtils.isTrue(rule.getLabelIsExpression())) {
						//check label expression
						expressionEvaluator.evaluate(productContext, 
							rule.getLabel().getTranslatedValue(), 
							String.class);
					}
					
					Money price = new Money( expressionEvaluator.evaluate(productContext, 
						rule.getPriceExpression(), Float.class),
						currency.getCurrencyCode());
					
					Integer quantity = expressionEvaluator.evaluate(productContext, 
							rule.getQuantityExpression(), Integer.class);
					
					Discount discount = getLineItemDiscount(productContext,
							rule.getCode(), 
							order);
					
					
					
					//add line item
					LineItem lineItem = new LineItem();
					lineItem.setCode(rule.getCode());
					lineItem.setOrder(rule.getOrder());
					
					if (quantity!=null) {
						BigDecimal total = price.getAmount().multiply( new BigDecimal(quantity) );
						lineItem.setSubtotalPrice(new Money(total, currency.getCurrencyCode()));
						if (discount!=null)
							total = total.subtract( discount.getDiscountValue(total) );
						
						lineItem.setPrice(price);
						lineItem.setDiscount(discount);
						lineItem.setQuantity(quantity);
						lineItem.setTotalPrice(new Money(total, currency.getCurrencyCode()));
					}
					lineItem.setLabelIsExpression(rule.getLabelIsExpression());
					lineItem.setLabel(rule.getLabel());
					lineItem.setProductPrice(productPrice);
					productPrice.getLineItems().add(lineItem);
					
				}
			} catch (Exception e) {
				logError(rule, e);
			}
		}
	}
	
	private void applyLineItemGroupingRules(ProductPrice productPrice, ProductContext productContext, Order order, Currency currency) {
		
		List<LineItem> groupedItems = new ArrayList<LineItem>();
		for(PriceRule rule:priceRules)
		{
			if (rule.getType()!=PriceRuleType.LineItemGroupingRule)
				continue;
			
			try {
				Boolean condition = expressionEvaluator.evaluate(productContext, 
						rule.getConditionExpression(), 
						Boolean.class);
				if (!BooleanUtils.isTrue(condition)) 
					continue;
					
				LineItem group = new LineItem();
				group.setPrice( new Money(0.0f, currency.getCurrencyCode()) );
				group.setCode( rule.getCode() );
				group.setProductPrice(productPrice);
				group.setOrder( rule.getOrder() );
				group.setLabel( rule.getLabel() );
				
				if (BooleanUtils.isTrue(rule.getLabelIsExpression())) {
					//check label expression
					expressionEvaluator.evaluate(productContext, 
						rule.getLabel().getTranslatedValue(), 
						String.class);
				}
				group.setLabelIsExpression(rule.getLabelIsExpression());
				
				if (rule.getPriceExpression()!=null) {
					Money price = new Money( expressionEvaluator.evaluate(productContext, 
						rule.getPriceExpression(), Float.class),
						currency.getCurrencyCode());
					group.setPrice( price );
				}
				
				for(LineItem lineItem:productPrice.getLineItems()) {
					if (rule.getGroup().contains(lineItem.getCode()))
					{
						if (rule.getPriceExpression()==null)
						{
							group.getPrice().setAmount(  
								group.getPrice().getAmount().add( lineItem.getPrice().getAmount() ) );
						}
						groupedItems.add(lineItem);
					}
				}
				
				Integer quantity = expressionEvaluator.evaluate(productContext, 
						rule.getQuantityExpression(), Integer.class);
				
				Discount discount = getLineItemDiscount( productContext,
						group.getCode(), 
						order);
				
				BigDecimal total = group.getPrice().getAmount().multiply(new BigDecimal(quantity));
				group.setSubtotalPrice(new Money(total, currency.getCurrencyCode()));
				if (discount!=null)
					total = total.subtract( discount.getDiscountValue(total) );
				group.setQuantity(quantity);
				group.setDiscount(discount);
				group.setTotalPrice(new Money(total, currency.getCurrencyCode()));
				
				productPrice.getLineItems().add(group);
			} catch (Exception e) {
				logError(rule, e);
			}
		}
		productPrice.getLineItems().removeAll(groupedItems);
		//sort by order
		Collections.sort(productPrice.getLineItems(), new Comparator<LineItem>(){
		     public int compare(LineItem o1, LineItem o2){
		         if(o1.getOrder() == o2.getOrder())
		             return 0;
		         return o1.getOrder() < o2.getOrder() ? -1 : 1;
		     }
		});
		
	}
	
	private void applyCartPriceRules(Order order, OrderContext orderContext, Currency currency) {
		for(PriceRule rule:priceRules)
		{
			if (PriceRuleType.CartRule != rule.getType())
				continue;
			try {
				Boolean condition = expressionEvaluator.evaluate(orderContext, 
						rule.getConditionExpression(), 
						Boolean.class);
				
				if (BooleanUtils.isTrue(condition)) {
					OrderAdjustment item = new OrderAdjustment();
					
					if (BooleanUtils.isTrue(rule.getLabelIsExpression())) {
						//check label expression
						expressionEvaluator.evaluate(orderContext, 
							rule.getLabel().getTranslatedValue(), 
							String.class);
					}
					
					Money price = new Money(
						expressionEvaluator.evaluate(orderContext, rule.getPriceExpression(), Float.class),
						currency.getCurrencyCode());
					
					item.setAmount( price );
					item.setLabelIsExpression( rule.getLabelIsExpression() );
					item.setLabel( rule.getLabel() );
					item.setOrder( order );
					 
					order.getOrderAdjustments().add( item );
				}
			} catch (Exception e) {
				logError(rule, e);
			}
		}
		for(DiscountRule discountRule:getOrderDiscountRules(order, orderContext))
		{
			OrderAdjustment item = new OrderAdjustment();
			BigDecimal price = discountRule.getDiscount().getDiscountValue(
					order.getSubtotal().getAmount());
			
			item.setAmount( new Money( price.multiply(new BigDecimal(-1) ), currency.getCurrencyCode()));
			item.setLabelIsExpression(false);
			item.setLabel(discountRule.getLabel());
			item.setOrder(order);
			 
			order.getOrderAdjustments().add(item);
		}
	}
	
	private void applyTaxes(Order order, Currency currency) {
		List<TaxRate> taxRates = taxService.findTaxRates(order);
		boolean includeShipping = true;
		for(TaxRate taxRate:taxRates) {
			Boolean shouldIncludeShipping = taxRate.getIncludeShipping(); 
			if (BooleanUtils.isFalse(shouldIncludeShipping))
			{
				includeShipping = false;
			}
			OrderTax orderTax = new OrderTax();
			orderTax.setTaxRate(taxRate);
			BigDecimal tax = order.getSubtotalIncludingAdjustments().getAmount()
					.multiply( new BigDecimal(taxRate.getRate()) )
					.divide(new BigDecimal(100));
			tax.setScale(2, BigDecimal.ROUND_DOWN);
			orderTax.setTax(new Money(tax, currency.getCurrencyCode()));
			orderTax.setOrder(order);
			order.getTaxes().add(orderTax);
		}
		
		order.setShippingIncludedInTax(includeShipping);
	}
	
	private void calculateSubtotal(ProductPrice productPrice, Currency currency) {
		
		BigDecimal subtotal = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(LineItem li:productPrice.getLineItems()) {
			if (li.getTotalPrice()!=null)
				subtotal = subtotal.add( li.getTotalPrice().getAmount() );
		}
		BigDecimal adjustmentSubtotal = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(OrderAdjustment adjustment:productPrice.getOrderSubItemAdjustments()) {
			adjustmentSubtotal = adjustmentSubtotal.add( adjustment.getAmount().getAmount() );
		}
		productPrice.setSubtotal(new Money(subtotal, currency.getCurrencyCode()));
		productPrice.setSubtotalIncludingAdjustements(new Money(subtotal.add(adjustmentSubtotal), currency.getCurrencyCode()));
	}
	
	private void calculateSubtotal(Product product, Currency currency) {
		
		BigDecimal subtotal = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(ProductPrice productPrice:product.getProductPrices()) {
			calculateSubtotal(productPrice, currency);
			subtotal = subtotal.add( productPrice.getSubtotalIncludingAdjustements().getAmount() );
		}
		product.setSubtotal(new Money(subtotal, currency.getCurrencyCode()));
		for(Product p: product.getChildren()) {
			calculateSubtotal(p, currency);
		}
		
		//calculate the total price of the product and its duplicates
		BigDecimal total = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(Product p: product.getProductAndChildren()) {
			total = total.add( p.getSubtotal().getAmount() );
		}
		product.setTotal(new Money(total, currency.getCurrencyCode()));
	}
	
	private void calculateSubtotal(OrderItem orderItem, Currency currency) {
		
		BigDecimal subtotal = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(Product product:orderItem.getProduct().getProductAndChildren())
		{
			for(ProductPrice productPrice:product.getProductPrices()) {
				calculateSubtotal(productPrice, currency);
				subtotal = subtotal.add( 
					productPrice.getSubtotalIncludingAdjustements().getAmount() );
			}
		}
		orderItem.setSubtotal(new Money(subtotal, currency.getCurrencyCode()));
	}
	
	private void calculateSubtotal(Order order, Currency currency) {
		
		BigDecimal subtotal = new Money(0.0f, currency.getCurrencyCode()).getAmount();
		for(OrderItem orderItem: order.getOrderItems())
		{
			calculateSubtotal(orderItem, currency);
			subtotal = subtotal.add( orderItem.getSubtotal().getAmount() );
			
		}
		order.setSubtotal(new Money(subtotal, currency.getCurrencyCode()));
	}
	
	private void calculateAdjustments(Order order, Currency currency) {
		
		BigDecimal total = order.getSubtotal().getAmount();
		for(OrderAdjustment subtotalItem: order.getOrderAdjustments())
		{
			total = total.add( subtotalItem.getAmount().getAmount() );
		}
		if (order.getShippingCost()!=null && BooleanUtils.isTrue(order.getShippingIncludedInTax())) {
			total = total.add(order.getShippingCost().getAmount());
		}
		
		//subtotal with adjustments cannot be less than 0
		if (total.compareTo(BigDecimal.ZERO)<0)
			total = BigDecimal.ZERO;
		
		order.setSubtotalIncludingAdjustments(new Money(total, currency.getCurrencyCode()) );
	}
	
	private void calculateGrandTotal(Order order, Currency currency) {
		
		BigDecimal total = order.getSubtotalIncludingAdjustments().getAmount();
		
		//add taxes and shipping
		for(OrderTax orderTax:order.getTaxes()) 
		{
			total = total.add( orderTax.getTax().getAmount() );
		}
		
		if (order.getShippingCost()!=null && !BooleanUtils.isTrue(order.getShippingIncludedInTax())) {
			total = total.add( order.getShippingCost().getAmount() );
		}
		
		//grand total cannot be less than 0
		if (total.compareTo(BigDecimal.ZERO)<0)
			total = BigDecimal.ZERO;
		
		order.setTotal( new Money(total, currency.getCurrencyCode()) );
	}
	
	private boolean isCouponValid(DiscountRule rule, Order order) {
		if (!StringUtils.equals(rule.getCouponCode(), order.getCouponCode()))
			return false;
		
		long couponUses = orderRepository.countByCouponCodeAndUserAndIdNot(
				order.getCouponCode(), order.getUser(), order.getId());
		return couponUses==0;
	}
	
	private void logError(PriceRule rule, Exception ex) {
		log.error("Error while evaluating price rule. ID=" + rule.getId() + 
				", CODE=" + rule.getCode() + ". " +	ex.getMessage());
	}
	
	private void logError(DiscountRule rule, Exception ex) {
		log.error("Error while evaluating discount rule. ID=" + rule.getId() + ". " +	ex.getMessage());
	}
}
