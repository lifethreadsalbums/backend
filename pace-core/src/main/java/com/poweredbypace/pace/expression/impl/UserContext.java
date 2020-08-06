package com.poweredbypace.pace.expression.impl;

import java.util.HashMap;

import org.mozilla.javascript.ScriptableObject;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.TCountry;
import com.poweredbypace.pace.domain.TState;
import com.poweredbypace.pace.domain.user.Group;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.ExpressionContext;

public class UserContext extends HashMap<String, Object> implements ExpressionContext {

	private static final long serialVersionUID = 1117724307758500833L;

	public UserContext(User user) {
		this.put("userId", user.getId());
		this.put("email", user.getEmail());
		this.put("currency", user.getCurrency());
		this.put("group", new UserGroup(user.getGroup()));
		this.put("billingAddress", new AddressObject(user.getBillingAddress()));
		this.put("shippingAddress", new AddressObject(user.getShippingAddress()));
	}
	
	public static class AddressObject extends ScriptableObject {

		private static final long serialVersionUID = 6152507602711894113L;

		public AddressObject(Address address) {
			if (address!=null) {
				TCountry country = address.getCountry();
				TState state = address.getState();
				this.defineProperty("countryCode", country!=null ? country.getIsoCountryCode() : null, READONLY);
				this.defineProperty("country", country!=null ? country.getName() : null, READONLY);
				this.defineProperty("state", state!=null ? state.getName() : null, READONLY);
				this.defineProperty("city", address.getCity(), READONLY);
				this.defineProperty("addressLine1", address.getAddressLine1(), READONLY);
				this.defineProperty("addressLine2", address.getAddressLine1(), READONLY);
			}
		}
		
		@Override
		public String getClassName() {
			return "Address";
		}
	
	}
	
	public static class UserGroup extends ScriptableObject {
		private static final long serialVersionUID = 2410827197368413173L;
		
		public UserGroup(Group group) {
			if (group!=null) {
				this.defineProperty("name", group.getName(), READONLY);
				this.defineProperty("id", group.getId(), READONLY);
			}
		}

		@Override
		public String getClassName() {
			return "Group";
		}
	}
	
	public static class UserObject extends ScriptableObject {
		private static final long serialVersionUID = 6152507602711894113L;

		public UserObject(User user) {
			if (user!=null) {
				this.defineProperty("userId", user.getId(), READONLY);
				this.defineProperty("email", user.getEmail(), READONLY);
			}
		}
		
		@Override
		public String getClassName() {
			return "User";
		}
	
	}
}
