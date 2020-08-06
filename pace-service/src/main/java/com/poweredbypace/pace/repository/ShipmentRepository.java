package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment,Long>{
	
	List<Shipment> findByOrder(Order order);
	
}
