package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.TProductOptionType;
import com.poweredbypace.pace.domain.store.Store;

public interface TProductOptionTypeRepository extends JpaRepository<TProductOptionType, Long> {
	TProductOptionType findBySystemAttributeAndStore(SystemAttribute systemAttribute, Store store);
}
