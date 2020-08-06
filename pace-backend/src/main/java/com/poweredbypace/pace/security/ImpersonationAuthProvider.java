package com.poweredbypace.pace.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.service.UserService;

@SuppressWarnings("deprecation")
public class ImpersonationAuthProvider implements AuthenticationProvider {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		String username = auth.getName();
		String password = (String) auth.getCredentials();
		
		if (username!=null && username.contains("/") && password!=null) {
			String[] names = username.split("/");
			if (names.length==2) {
				
				String hashedPassword = passwordEncoder.encodePassword(password, null);
				User admin = userService.getByEmail(names[0]);
				
				if (admin!=null && admin.hasRole("ROLE_ADMIN") && StringUtils.equals(hashedPassword, admin.getPassword()))
				{
					UserDetails user = ((UserDetailsService)userService).loadUserByUsername(names[1]);
					if (user!=null) {
						return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
					}
				}
			}
			
		}
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
