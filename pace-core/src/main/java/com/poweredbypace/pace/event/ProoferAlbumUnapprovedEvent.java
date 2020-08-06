package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferAlbumUnapprovedEvent extends ProoferEvent {
	
	public ProoferAlbumUnapprovedEvent() { }

	public ProoferAlbumUnapprovedEvent(Product product) {
		super(product);
	}
}
