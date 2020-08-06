package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.ProoferTrackingEvent;
import com.poweredbypace.pace.domain.layout.ProoferTrackingEvent.ProoferTrackingEventType;

public interface ProoferEventRepository extends JpaRepository<ProoferTrackingEvent, Long> {
	
	List<ProoferTrackingEvent> findByProductAndTypeOrderByDateDesc(Product product, ProoferTrackingEventType type);
	List<ProoferTrackingEvent> findByProduct(Product product);
	
}
