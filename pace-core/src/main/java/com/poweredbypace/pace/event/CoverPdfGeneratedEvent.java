package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class CoverPdfGeneratedEvent extends ProductEvent {

	public CoverPdfGeneratedEvent() { }

	public CoverPdfGeneratedEvent(Product product) {
		super(product);
	}
	
}
