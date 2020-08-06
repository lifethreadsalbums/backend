package com.poweredbypace.pace.expression.impl;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;

public class ShippingProductContext extends ProductContext {

	private static final long serialVersionUID = -8181222156893438278L;

	public ShippingProductContext(Product product, Order order) {
		super(product);
		
		Address shippingAddress = order.getDropShippingAddress()!=null ?
			order.getDropShippingAddress() : order.getShippingAddress();
		
		this.put("dropShipping", order.getDropShippingAddress()!=null);
		this.put("shippingAddress", new UserContext.AddressObject(shippingAddress));
		this.put("billingAddress", new UserContext.AddressObject(order.getBillingAddress()));
		this.put("user", new UserContext.UserObject(order.getUser()));
	}

}
