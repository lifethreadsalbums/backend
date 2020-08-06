package com.poweredbypace.pace.expression.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;

public class ShippingOrderContext extends OrderContext {

	private static final long serialVersionUID = -8181222156893438278L;

	public ShippingOrderContext(Order order) {
		super(order);
		
		int totalProducts = 0;
		List<Product> products = new ArrayList<Product>();
		for(OrderItem item:order.getOrderItems()) {
			for(Product p:item.getProduct().getProductAndChildren()) {
				if (BooleanUtils.isTrue(p.getPrototypeProduct().getFreeShipping()))
					continue;
				if (p.getQuantity()!=null) {
					totalProducts += p.getQuantity();
					products.add(p);
				}
			}
		}
		
		Address shippingAddress = order.getDropShippingAddress()!=null ?
			order.getDropShippingAddress() : order.getShippingAddress();
		this.put("shippingAddress", new UserContext.AddressObject(shippingAddress));
		this.put("billingAddress", new UserContext.AddressObject(order.getBillingAddress()));
		this.put("totalProducts", totalProducts);
		this.put("user", new UserContext.UserObject(order.getUser()));
		this.put("products", products);
		this.put("order", order);
	}

}
