package com.poweredbypace.pace.expression.impl;

import java.util.HashMap;
import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.expression.ExpressionContext;

public class BuldProductStateContext extends HashMap<String, Object> implements ExpressionContext {

	private static final long serialVersionUID = 3113611492352684942L;

	public BuldProductStateContext(List<Product> products, ProductState state) {
		this.put("state", state.toString());
		this.put("dropShipAddress", null);
		this.put("shippingOption", null);
		this.put("carrier", null);
		Order order = null;
		for(Product p:products) {
			OrderItem orderItem = p.getParent()!=null ? p.getParent().getOrderItem() : p.getOrderItem();
			if (orderItem!=null) {
				order = orderItem.getOrder();
				this.put("carrier", p.getProductOptionCode("carrier"));
				break;
			}
		}
		if (order!=null) {
			if (order.getDropShippingAddress()!=null) {
				this.put("dropShipAddress", new UserContext.AddressObject(order.getDropShippingAddress()));
			}
			if (order.getShippingOption()!=null) {
				this.put("shippingOption", order.getShippingOption().getCode());
			}
		}
	}
}
