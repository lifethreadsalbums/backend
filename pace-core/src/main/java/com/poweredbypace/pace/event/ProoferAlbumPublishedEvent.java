package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferAlbumPublishedEvent extends ProoferEvent {
	
	public ProoferAlbumPublishedEvent() { }

	public ProoferAlbumPublishedEvent(Product product) {
		super(product);
	}
}
