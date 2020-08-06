package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey,Long>{

	ApiKey findByUsername(String username);
	
}
