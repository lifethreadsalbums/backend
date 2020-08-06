package com.poweredbypace.pace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.impl.UserContext;
import com.poweredbypace.pace.repository.StoreRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.StoreService;
import com.poweredbypace.pace.service.UserService;

@Service
public class StoreServiceImpl implements StoreService {
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public Store getUserStore(User user) {
		String storeCode = ruleService.getRuleValue(new UserContext(user), "CURRENT_STORE", String.class);
		if (storeCode!=null) {
			return storeRepository.findByCode(storeCode);
		}
		return null;
	}

	@Override
	public Store getCurrentStore(String domainName) {
		User user = userService.getPrincipal();
		Store store = null;
		if (user!=null) 
			store = getUserStore(user);
		if (store==null)
			store = storeRepository.findByDomainName(domainName);
		if (store==null) 
			store = storeRepository.findByIsDefaultTrue();
		
		return store;
	}

}
