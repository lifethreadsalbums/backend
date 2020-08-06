package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.Product;

public interface AttachmentRepository extends JpaRepository<Attachment,Long>{

	List<Attachment> findByProduct(Product product);
	List<Attachment> findByProductAndType(Product product, AttachmentType type);
	Attachment findOneByProductAndType(Product product, AttachmentType type);
	
}
