package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.layout.LayoutSnapshot;

public interface LayoutSnapshotRepository extends JpaRepository<LayoutSnapshot, Long> {
	
	List<LayoutSnapshot> findByLayoutId(Long layoutId);
	
}
