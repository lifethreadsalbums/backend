package com.poweredbypace.pace.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.poweredbypace.pace.domain.Product;

public interface BatchSubmissionService {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void submitBatch();
	
	public void sendTicket(Product p);
	
}
