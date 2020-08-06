package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.TProductOptionValue;

public interface TProductOptionValueRepository extends JpaRepository<TProductOptionValue, Long> {
	
	List<TProductOptionValue> findByCode(String code);
	
}
