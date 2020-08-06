package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.TransactionLogEntry;

public interface TransactionLogEntryRepository extends JpaRepository<TransactionLogEntry, Long> {
	List<TransactionLogEntry> findByOrder(Order order);
}
