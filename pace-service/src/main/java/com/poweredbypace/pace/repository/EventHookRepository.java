package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.EventHook;

public interface EventHookRepository extends JpaRepository<EventHook, Long> {
	@SuppressWarnings("rawtypes")
	List<EventHook> findByEventClass(Class eventClass);
}
