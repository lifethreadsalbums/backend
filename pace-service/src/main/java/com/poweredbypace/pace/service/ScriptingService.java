package com.poweredbypace.pace.service;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

public interface ScriptingService {

	@PreAuthorize("hasRole('ROLE_SUPER')")
	void runScript(String script, Map<String,Object> params);
	
	@PreAuthorize("hasRole('ROLE_SUPER')")
	void runScript(String script);
	
	@PreAuthorize("hasRole('ROLE_SUPER')")
	<T> T runScript(String script, Map<String,Object> params, Class<T> resultType);
	
}
