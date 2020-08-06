package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.View;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.env.Env;

public interface ViewService {
	View getView(User user);
	View getDefault();
	Env getEnv();
}
