package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.GenericRule;

public interface GenericRuleRepository extends JpaRepository<GenericRule, Long> {
	List<GenericRule> findByCode(String code);
}
