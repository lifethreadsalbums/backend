package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	OrderItem findByProduct(Product product);
	
}
