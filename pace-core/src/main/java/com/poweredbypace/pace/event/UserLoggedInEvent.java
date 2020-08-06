package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public class UserLoggedInEvent extends UserEvent {

	public UserLoggedInEvent() { }

	public UserLoggedInEvent(User user, Store store) {
		super(user, store);
	}
}
