package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.mail.EmailTemplate;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate,Long>{

	EmailTemplate findByName(String name);
	
}
