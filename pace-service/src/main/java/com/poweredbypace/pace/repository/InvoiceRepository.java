package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.Order;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Invoice findByOrder(Order order);
	
}
