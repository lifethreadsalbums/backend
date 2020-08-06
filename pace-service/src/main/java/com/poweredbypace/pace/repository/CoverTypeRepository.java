package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.layout.CoverType;

public interface CoverTypeRepository extends JpaRepository<CoverType,Long>{

	CoverType findByCode(String code);
	
}
