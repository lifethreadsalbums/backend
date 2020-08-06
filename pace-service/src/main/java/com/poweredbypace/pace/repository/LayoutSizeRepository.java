package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.domain.layout.LayoutSize;

public interface LayoutSizeRepository extends JpaRepository<LayoutSize,Long>{

	List<LayoutSize> findByCode(String code);
	List<LayoutSize> findByCodeAndCoverTypeNull(String code);
	LayoutSize findByCodeAndCoverType(String code, CoverType coverType);
	
}
