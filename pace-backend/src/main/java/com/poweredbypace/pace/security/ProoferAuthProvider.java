package com.poweredbypace.pace.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.repository.ProoferSettingsRepository;

public class ProoferAuthProvider implements AuthenticationProvider {
	
	final String PROOFER_USER_PREFIX = "proofer-user-";
	
	@Autowired
	private ProoferSettingsRepository prooferSettingsRepository;
	

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		String username = auth.getName();
		String password = (String) auth.getCredentials();
		
		if (username!=null && username.startsWith(PROOFER_USER_PREFIX) && password!=null) {
			long productId = Long.parseLong(username.replaceFirst(PROOFER_USER_PREFIX, ""));
			ProoferSettings ps = prooferSettingsRepository.findByProductId(productId);
			if (ps==null) return null;
			User user = ps.getUser();
			if (StringUtils.equals(ps.getPassword(), password)) {
				return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
			}
		}
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
