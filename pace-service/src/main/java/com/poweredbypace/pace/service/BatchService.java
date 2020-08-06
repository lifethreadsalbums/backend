package com.poweredbypace.pace.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.poweredbypace.pace.domain.Batch;

public interface BatchService extends CrudService<Batch> {
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Batch getPendingBatch();

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Batch save(Batch entity);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<Batch> save(List<Batch> entities);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Batch findOne(long id);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<Batch> findAll();
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void delete(Batch entity);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void delete(List<Batch> entities);
	
}
