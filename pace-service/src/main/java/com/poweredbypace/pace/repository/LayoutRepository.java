package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.layout.Layout;

public interface LayoutRepository extends JpaRepository<Layout,Long>{
	
	List<Layout> findByMainLayout(Layout layout);
	Layout findByMainLayoutAndRevision(Layout layout, Integer revision);
	
}
