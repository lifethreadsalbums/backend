package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public class UserRegisteredEvent extends UserEvent {

	public UserRegisteredEvent() { }

	public UserRegisteredEvent(User user, Store store) {
		super(user, store);
	}
}
