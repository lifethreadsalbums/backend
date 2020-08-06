package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.TaxRate;
import com.poweredbypace.pace.domain.order.Order;


public interface TaxService{

	public List<TaxRate> findTaxRates(Order order);
	
}