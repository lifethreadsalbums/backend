package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferAlbumApprovedEvent extends ProoferEvent {
	
	public ProoferAlbumApprovedEvent() { }

	public ProoferAlbumApprovedEvent(Product product) {
		super(product);
	}
}
