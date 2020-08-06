package com.poweredbypace.pace.shipping;

import java.util.List;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;

public interface PackingStrategy {
	
	List<ShippingPackage> pack(Order order);

}
