package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.store.Store;


public interface StoreRepository extends JpaRepository<Store, Long>
{
	Store findByCode(String code);
	Store findByDomainName(String domainName);
	Store findByIsDefaultTrue();
}
