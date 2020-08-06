package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.CustomScript;

public interface CustomScriptRepository extends JpaRepository<CustomScript, Long> {

	CustomScript findByCode(String code);
	
}
