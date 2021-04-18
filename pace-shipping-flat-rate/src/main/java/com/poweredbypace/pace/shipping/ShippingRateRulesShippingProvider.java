package com.poweredbypace.pace.shipping;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.ShippingRateRule;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ShippingOrderContext;
import com.poweredbypace.pace.repository.ShippingRateRuleRepository;
import com.poweredbypace.pace.service.ScriptingService;

public class ShippingRateRulesShippingProvider extends ShippingProvider {
	
	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private ShippingRateRuleRepository rulesRepo;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private ScriptingService scriptingService;

	private static String LOCAL_PICKUP = "LOCAL_PICKUP";
	
	public ShippingRateRulesShippingProvider(String providerId) {
		super(providerId);
	}

	@Override
	@Transactional
	public RateShippingResponse rate(Shipment shipment) {
		RateShippingResponse response = new RateShippingResponse();
		response.setShipment(shipment);
		
		//get rules
		List<ShippingRateRule> rules = rulesRepo.findByEnabledTrue(new Sort("order"));
		ShippingOrderContext ctx = new ShippingOrderContext(shipment.getOrder());
		
		for(ShippingRateRule rule:rules) {
//			Boolean condition = expressionEvaluator.evaluate(ctx, 
//				rule.getConditionExpression(), Boolean.class);
			if (rule.getCode().equals(LOCAL_PICKUP)) {
				continue;
			}
			Boolean condition = false;
			try {
				condition = scriptingService.runScript(rule.getConditionExpression(), ctx, Boolean.class);
			} catch (Exception ex) {
				log.error("Error while evaluating condition expression of shipping rule "+rule.getCode()+": "+ex.getLocalizedMessage());
			}
			
			if (BooleanUtils.isTrue(condition)) {
				try {
					Float rateResult = scriptingService.runScript(rule.getRateExpression(), ctx, Float.class);
//					Money rate = new Money( 
//						expressionEvaluator.evaluate(ctx, rule.getRateExpression(), Float.class) );
					Money rate = new Money(rateResult);
					
					RateShippingResponseEntry entry = new RateShippingResponseEntry();
					entry.setMoney(rate);
					
					ShippingOption shippingOption = new ShippingOption();
					shippingOption.setCode(rule.getCode());
					shippingOption.setName(rule.getLabel().getTranslatedValue());
					
					if (rule.getChristmasShippingFrom()!=null && rule.getChristmasShippingTo()!=null) {
						Calendar today = Calendar.getInstance();
						today.setTime(new Date());
						
						Calendar dec25 = Calendar.getInstance();
						dec25.set(today.get(Calendar.YEAR), 11, 25);
						
						Calendar from = Calendar.getInstance();
						from.setTime(rule.getChristmasShippingFrom());
						from.set(Calendar.YEAR, today.get(Calendar.YEAR));
						
						Calendar to = Calendar.getInstance();
						to.setTime(rule.getChristmasShippingTo());
						to.set(Calendar.YEAR, today.get(Calendar.YEAR));
						
						if (today.before(dec25) && today.after(from)) {
							shippingOption.setChristmasShipping( today.before(to) );
						}
					}
					
					if (BooleanUtils.isTrue(rule.getLabelIsExpression())) {
						String result = expressionEvaluator.evaluate(ctx, rule.getLabel().getTranslatedValue(), String.class);
						shippingOption.setName(result);
					}
					
					shippingOption.setProviderId(getProviderId());
					entry.setShippingOption(shippingOption);
					response.getEntries().add(entry);
				} catch(Exception ex) {
					log.error("Error while evaluating shipping rule "+rule.getCode()+": "+ex.getLocalizedMessage());
				}
			}	
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
