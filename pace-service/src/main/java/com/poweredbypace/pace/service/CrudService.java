package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.BaseEntity;

public interface CrudService<T extends BaseEntity> {
	
	T save(T entity);
	List<T> save(List<T> entities);
	T findOne(long id);
	List<T> findAll();
	List<T> findAll(List<Long> ids);
	void delete(long id);
	void delete(T entity);
	void delete(List<T> entities);

}
