package com.poweredbypace.pace.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;


public class ApplicationEvent {
	
	private User currentUser;
	
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	public ApplicationEvent() {	}

}
