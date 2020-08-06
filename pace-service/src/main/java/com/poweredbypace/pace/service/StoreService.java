package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public interface StoreService {
	
	Store getUserStore(User user);
	Store getCurrentStore(String domainName);

}