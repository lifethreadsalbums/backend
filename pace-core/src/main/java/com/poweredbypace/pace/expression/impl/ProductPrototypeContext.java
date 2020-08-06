package com.poweredbypace.pace.expression.impl;

import java.util.HashMap;

import org.mozilla.javascript.ScriptableObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.ExpressionContext;

public class ProductPrototypeContext extends HashMap<String, Object> implements ExpressionContext {

	private static final long serialVersionUID = 1117724307758500833L;

	public ProductPrototypeContext(PrototypeProduct prototype) {
		
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() instanceof User)
				this.put("currentUser", new UserObject((User)auth.getPrincipal()));
		}
		
		this.put("code", prototype.getCode());
	}
	
	public static class UserObject extends ScriptableObject {

		private static final long serialVersionUID = -6611447604031872712L;

		public UserObject(User user) {
			this.defineProperty("groupId", user.getGroup().getId(), READONLY);
		}
		
		@Override
		public String getClassName() {
			return "User";
		}
	
	}
}
