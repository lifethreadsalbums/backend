package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.store.Store;


public interface PrototypeProductRepository extends JpaRepository<PrototypeProduct, Long>
{

	@Query("SELECT p FROM PrototypeProduct p WHERE p.code=?1 OR INSTR(p.tag, ?1)>0")
	PrototypeProduct findByCode(String code);
	
	@Query("SELECT p FROM PrototypeProduct p INNER JOIN p.stores s WHERE s IN (?1)")
	List<PrototypeProduct> findByStore(Store store);
	
	@Query("SELECT p FROM PrototypeProduct p INNER JOIN p.stores s WHERE p.isDefault=true AND s IN (?1)")
	PrototypeProduct findDefault(Store store);

}
