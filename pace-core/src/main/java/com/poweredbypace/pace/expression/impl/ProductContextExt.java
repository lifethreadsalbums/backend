package com.poweredbypace.pace.expression.impl;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;

public class ProductContextExt extends ProductContext {
	
	private static final long serialVersionUID = -4762841627687874100L;

	public ProductContextExt(Product product)
	{
		super(product);
		this.put("dropShipAddress", null);
		this.put("shippingOption", null);
		if (product.getOrderItem()!=null) {
			Order order = product.getOrderItem().getOrder();
			if (order.getDropShippingAddress()!=null) {
				this.put("dropShipAddress", new UserContext.AddressObject(order.getDropShippingAddress()));
			}
			if (order.getShippingOption()!=null) {
				this.put("shippingOption", order.getShippingOption().getCode());
			}
		}
	}
	
}
