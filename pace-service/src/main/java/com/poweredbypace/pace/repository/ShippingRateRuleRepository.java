package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.ShippingRateRule;

public interface ShippingRateRuleRepository extends JpaRepository<ShippingRateRule,Long>{
	
	List<ShippingRateRule> findByEnabledTrue(Sort sort);
	
}
