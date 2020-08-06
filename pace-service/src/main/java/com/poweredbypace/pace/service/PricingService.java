package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;

public interface PricingService {

	void executePricing(Product product);
	void executePricing(Order order);
	void checkCoupon(Order order, String couponCode);

}