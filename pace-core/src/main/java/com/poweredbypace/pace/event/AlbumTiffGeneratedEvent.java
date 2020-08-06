package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class AlbumTiffGeneratedEvent extends ProductEvent {

	public AlbumTiffGeneratedEvent() { }

	public AlbumTiffGeneratedEvent(Product product) {
		super(product);
	}
	
}
