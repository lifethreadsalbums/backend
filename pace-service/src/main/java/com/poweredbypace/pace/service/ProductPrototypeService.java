package com.poweredbypace.pace.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.store.Store;

public interface ProductPrototypeService {
	PrototypeProduct getById(long id);
	PrototypeProduct getByCode(String code);
	PrototypeProduct getDefault(Store store);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void prepopulateCache();
}
