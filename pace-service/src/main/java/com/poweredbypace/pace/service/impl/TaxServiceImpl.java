package com.poweredbypace.pace.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Address.AddressType;
import com.poweredbypace.pace.domain.DestinationZone;
import com.poweredbypace.pace.domain.TState;
import com.poweredbypace.pace.domain.Tax;
import com.poweredbypace.pace.domain.TaxRate;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.OrderContext;
import com.poweredbypace.pace.repository.TaxRepository;
import com.poweredbypace.pace.service.TaxService;


@Service
public class TaxServiceImpl implements TaxService {

	private final Log log = LogFactory.getLog(PricingServiceImpl.class);
	
	@Autowired
	private TaxRepository taxRepo;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
		
	@Override
	public List<TaxRate> findTaxRates(Order order) {
		List<TaxRate> result = new ArrayList<TaxRate>();
		OrderContext orderContext = new OrderContext(order);
		for(Tax tax:taxRepo.findAll())
		{
			Address address = order.getAddress( tax.getRatesDependsOn() );
			if (tax.getRatesDependsOn()==AddressType.ShippingAddress) {
				Address dropShippingAddress = order.getDropShippingAddress();
				if (dropShippingAddress!=null)
					address = dropShippingAddress;
			}
			
			if (address!=null) {
				for(TaxRate taxRate:tax.getTaxRates())
				{
					DestinationZone zone = taxRate.getDestinationZone();
					if (isAddressInZone(address, zone)) {
						
						if (taxRate.getConditionExpression()!=null) {
							//evaluate condition
							try {
								Boolean condition = expressionEvaluator.evaluate(orderContext, 
										taxRate.getConditionExpression(), 
										Boolean.class);
								if (!BooleanUtils.isTrue(condition))
									continue;
							} catch (Exception ex) {
								log.error("", ex);
								continue;
							}
						}
						
						result.add(taxRate);
					}
				}
			}
		}
		//sort by priority
		Collections.sort(result, new Comparator<TaxRate>() {
			@Override
			public int compare(TaxRate o1, TaxRate o2) {
				return o1.getTax().getPriority().compareTo(o2.getTax().getPriority());
			}
		});
		
		return result;
	}
	
	private boolean isAddressInZone(Address address, DestinationZone zone) {
		boolean result = false;
		if (address.getCountry()!=null && address.getCountry().equals(zone.getCountry())) {
			for(TState state:zone.getStates() )
			{
				if (address.getState().equals(state))
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

}
