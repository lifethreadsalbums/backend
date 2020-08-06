package com.poweredbypace.pace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.ApiKey;
import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.domain.user.User.UserStatus;
import com.poweredbypace.pace.repository.ApiKeyRepository;
import com.poweredbypace.pace.repository.RoleRepository;

@Service("apiKeyService")
public class ApiKeyServiceImpl implements UserDetailsService {

	
	@Autowired
	private ApiKeyRepository apiKeyRepo;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ApiKey apiKey = apiKeyRepo.findByUsername(username);
		if (apiKey == null) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}
		User user = new User();
		user.setEmail(username);
		user.setPassword(apiKey.getPassword());
		user.setFirstName(apiKey.getDescription());
		user.setLastName(apiKey.getDescription());
		user.setStatus(UserStatus.Enabled);
		user.getRoles().add(roleRepository.findByName(Role.ROLE_ADMIN));
		
		return user;
	}

}
