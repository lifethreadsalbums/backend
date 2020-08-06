package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.layout.Element;

public interface ElementRepository extends JpaRepository<Element, Long> {
	
	
}
