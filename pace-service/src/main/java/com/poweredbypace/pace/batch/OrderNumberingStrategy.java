package com.poweredbypace.pace.batch;

import com.poweredbypace.pace.domain.order.Order;

public interface OrderNumberingStrategy {
	
	void assignOrderNumber(Order o);
	
}
