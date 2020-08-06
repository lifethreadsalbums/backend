package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class AlbumPdfGeneratedEvent extends ProductEvent {

	public AlbumPdfGeneratedEvent() { }

	public AlbumPdfGeneratedEvent(Product product) {
		super(product);
	}
	
}
