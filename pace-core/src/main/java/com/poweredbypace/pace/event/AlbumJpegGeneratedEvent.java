package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class AlbumJpegGeneratedEvent extends ProductEvent {

	public AlbumJpegGeneratedEvent() { }

	public AlbumJpegGeneratedEvent(Product product) {
		super(product);
	}
	
}
