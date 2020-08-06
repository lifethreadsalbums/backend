package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.ProoferSettings;

public interface ProoferSettingsRepository extends JpaRepository<ProoferSettings, Long> {
	
	List<ProoferSettings> findByEmail(String email);
	
	ProoferSettings findByProduct(Product product);
	
	@Query(value="select s from ProoferSettings s where s.product.id = ?1")
	ProoferSettings findByProductId(Long productId);

}
