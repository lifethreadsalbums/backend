package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferEvent extends ProductEvent {
	
	public ProoferEvent() { }

	public ProoferEvent(Product product) {
		super(product);
	}
}
