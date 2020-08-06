package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public class UserAccountEnabledEvent extends UserEvent {

	public UserAccountEnabledEvent() { }

	public UserAccountEnabledEvent(User user, Store store) {
		super(user, store);
	}
}
