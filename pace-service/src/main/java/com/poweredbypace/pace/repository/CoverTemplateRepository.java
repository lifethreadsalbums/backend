package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.layout.CoverTemplate;
import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.domain.layout.LayoutSize;

public interface CoverTemplateRepository extends JpaRepository<CoverTemplate,Long>{

//	@Query("select c from CoverTemplate c where c.prototypeProduct.id = ?1 and "
//					+ "c.coverType.id = ?2 and c.layoutSize.id = ?3")
//	List<CoverTemplate> findByProductPrototypeAndCoverTypeAndSize(Long productPrototypeId, Long coverTypeId, Long layoutSizeId);
//	
//	@Query("select c from CoverTemplate c where c.prototypeProduct.id = ?1 and "
//			+ "c.coverType.id = ?2")
//	List<CoverTemplate> findByProductPrototypeAndCoverType(Long productPrototypeId, Long coverTypeId);
	
	CoverTemplate findByPrototypeProductAndCoverTypeAndLayoutSize(PrototypeProduct prototypeProduct, CoverType coverType, LayoutSize layoutSize);
	
	List<CoverTemplate> findByPrototypeProductAndCoverType(PrototypeProduct prototypeProduct, CoverType coverType);
	
}
