package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Batch.BatchState;

public interface BatchRepository extends JpaRepository<Batch,Long>{

	List<Batch> findByState(BatchState state);
	List<Batch> findByState(BatchState state, Pageable pageRequest);
	
	long countByState(BatchState state);
	
}
