package com.poweredbypace.pace.expression.impl;

import java.util.HashMap;

import org.mozilla.javascript.ScriptableObject;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.ExpressionContext;

public class OrderContext extends HashMap<String, Object> implements ExpressionContext {

	private static final long serialVersionUID = 1985138923695609477L;

	public OrderContext(Order order) {
		super();
		
		this.put("user", new UserObject(order.getUser()));
		this.put("hasStudioSample", order.hasStudioSample());
		this.put("rush", order.getRush());
		this.put("subtotal", order.getSubtotal()!=null && order.getSubtotal().getAmount()!=null ?
				order.getSubtotal().getAmount().doubleValue() : null );
		this.put("shippingCost", order.getShippingCost()!=null && order.getShippingCost().getAmount()!=null ?
				order.getShippingCost().getAmount().doubleValue() : null );
		
	}
	
	public static class UserObject extends ScriptableObject {

		private static final long serialVersionUID = 519935524541462670L;
		
		public UserObject(User user) {
			this.defineProperty("email", user.getEmail(), READONLY);
			this.defineProperty("taxNumber", user.getTaxNumber(), READONLY);
			this.defineProperty("firstName", user.getFirstName(), READONLY);
			this.defineProperty("lastName", user.getLastName(), READONLY);
			this.defineProperty("role", user.getRoles().toArray(new Role[0])[0].getName(), READONLY);
		}
		
		@Override
		public String getClassName() {
			return "User";
		}
	
	}
}
