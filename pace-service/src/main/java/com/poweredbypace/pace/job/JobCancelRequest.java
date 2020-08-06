package com.poweredbypace.pace.job;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;

public class JobCancelRequest {
	
	private String id;
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
