package com.poweredbypace.pace.shipping;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.expression.impl.ShippingProductContext;
import com.poweredbypace.pace.repository.GenericRuleRepository;
import com.poweredbypace.pace.service.GenericRuleService;

public class FlatRateShippingProvider extends ShippingProvider {

	private static final String FLAT_RATE_SHIPPING_FIRST = "FLAT_RATE_SHIPPING_FIRST";
	private static final String FLAT_RATE_SHIPPING_NEXT = "FLAT_RATE_SHIPPING_NEXT";
	private static final String LOCAL_PICKUP_RATE = "LOCAL_PICKUP_RATE";
	private static final String FLAT_RATE_EXPEDITED = "FLAT_RATE_EXPEDITED";
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private GenericRuleRepository genericRuleRepo;

	
	public FlatRateShippingProvider(String providerId) {
		super(providerId);
	}

	@Override
	@Transactional
	public RateShippingResponse rate(Shipment shipment) {
		
		List<GenericRule> rules = genericRuleRepo.findByCode(FLAT_RATE_SHIPPING_FIRST);
		if (rules.size()==0) return null;
		
		RateShippingResponse response = new RateShippingResponse();
		response.setShipment(shipment);
		
		float maxRate = -1f;
		int totalProducts = 0;
		Product firstProduct = null;
		for(OrderItem item:shipment.getOrder().getOrderItems()) {
			for(Product p:item.getProduct().getProductAndChildren()) {
				if (BooleanUtils.isTrue(p.getPrototypeProduct().getFreeShipping()))
					continue;
				if (firstProduct==null) firstProduct = p;
				ShippingProductContext ctx = new ShippingProductContext(p, shipment.getOrder());
				Float rate = genericRuleService.getRuleValue(ctx, FLAT_RATE_SHIPPING_FIRST, Float.class);
				if (rate!=null && rate.floatValue()>maxRate)
					maxRate = rate;
				totalProducts += p.getQuantity();
			}
		}
		
		if (totalProducts>1) {
			ShippingProductContext ctx = new ShippingProductContext(firstProduct, shipment.getOrder());
			Float nextRate = genericRuleService.getRuleValue(ctx, FLAT_RATE_SHIPPING_NEXT, Float.class);
			if (nextRate!=null) {
				maxRate += nextRate * (totalProducts - 1);
			}
		}
		
		if (maxRate>=0f) {
			RateShippingResponseEntry entry = new RateShippingResponseEntry();
			entry.setMoney(new Money(maxRate));
			ShippingOption shippingOption = new ShippingOption();
			shippingOption.setCode("FLAT");
			shippingOption.setName("Flat rate shipping");
			shippingOption.setProviderId(getProviderId());
			shippingOption.setFreeShipping(maxRate==0f);
			entry.setShippingOption(shippingOption);
			response.getEntries().add(entry);
		}
			
		ShippingProductContext ctx = new ShippingProductContext(
				shipment.getOrder().getOrderItems().get(0).getProduct(), shipment.getOrder());
		
		Float expeditedRate = genericRuleService.getRuleValue(ctx, FLAT_RATE_EXPEDITED, Float.class);
		if (expeditedRate!=null && expeditedRate>0f) {
			
			if (totalProducts>1) {
				Float nextRate = genericRuleService.getRuleValue(ctx, FLAT_RATE_SHIPPING_NEXT, Float.class);
				if (nextRate!=null) {
					expeditedRate += nextRate * (totalProducts - 1);
				}
			}
			
			RateShippingResponseEntry entry = new RateShippingResponseEntry();
			entry.setMoney(new Money(expeditedRate));
			ShippingOption shippingOption = new ShippingOption();
			shippingOption.setCode("FLAT_RATE_EXPEDITED");
			shippingOption.setName("Expedited for Christmas (WILL ARRIVE IN TIME FOR CHRISTMAS)");
			shippingOption.setProviderId(getProviderId());
			entry.setShippingOption(shippingOption);
			response.getEntries().add(entry);
		}
		
		Float localPickupRate = genericRuleService.getRuleValue(ctx, LOCAL_PICKUP_RATE, Float.class);
		if (localPickupRate!=null && localPickupRate>0f) {
			RateShippingResponseEntry entry = new RateShippingResponseEntry();
			entry.setMoney(new Money(localPickupRate));
			ShippingOption shippingOption = new ShippingOption();
			shippingOption.setCode("LOCAL_PICKUP");
			shippingOption.setName("Local Pick Up");
			shippingOption.setProviderId(getProviderId());
			entry.setShippingOption(shippingOption);
			response.getEntries().add(entry);
		}
		
		response.setShipperName("PACE");
		response.setResponseEnum(ShippingResponse.ResponseEnum.OK);
		
		return response;
	}

	@Override
	public ShippingResponse ship(Shipment shipment,
			ShippingOption shippingOption) {
		return null;
	}

	@Override
	public TrackingResponse track(String trackingId) {
		return null;
	}

}
