package com.poweredbypace.pace.expression.impl;

import java.util.HashMap;

import org.mozilla.javascript.ScriptableObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.ExpressionContext;

public class InvoiceContext extends HashMap<String, Object> implements ExpressionContext {

	private static final long serialVersionUID = -9214372587107964757L;


	public InvoiceContext(Invoice invoice) {
		
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() instanceof User)
				this.put("currentUser", new UserObject((User)auth.getPrincipal()));
		}
		this.put("user", invoice.getOrder().getUser());
		this.put("orderNumber", invoice.getOrder().getOrderNumber());
		this.put("firstProductName", invoice.getOrder()
				.getOrderItems().get(0).getProduct().getName());
		
	}
	
	
	public static class UserObject extends ScriptableObject {

		private static final long serialVersionUID = -6611447604031872712L;

		public UserObject(User user) {
			this.defineProperty("isAdmin", user.isAdmin(), READONLY);
			this.defineProperty("firstName", user.getFirstName(), READONLY);
			this.defineProperty("lastName", user.getLastName(), READONLY);
		}
		
		@Override
		public String getClassName() {
			return "User";
		}
	
	}
	
}
