package com.poweredbypace.pace.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleStoreSerializer;
import com.poweredbypace.pace.json.SimpleUserSerializer;

public class UserEvent extends ApplicationEvent {

	private User user;
	private Store store;
	
	@JsonSerialize(using=SimpleStoreSerializer.class)
	public Store getStore() {
		return store;
	}
	
	public void setStore(Store store) {
		this.store = store;
	}

	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UserEvent() { }

	public UserEvent(User user, Store store) {
		this.user = user;
		this.store = store;
	}
}
