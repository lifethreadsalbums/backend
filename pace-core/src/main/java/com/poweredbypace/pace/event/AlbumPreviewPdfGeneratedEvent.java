package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class AlbumPreviewPdfGeneratedEvent extends ProductEvent {

	public AlbumPreviewPdfGeneratedEvent() { }

	public AlbumPreviewPdfGeneratedEvent(Product product) {
		super(product);
	}
	
}
