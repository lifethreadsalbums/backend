package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferEditsPendingEvent extends ProoferEvent {
	
	public ProoferEditsPendingEvent() { }

	public ProoferEditsPendingEvent(Product p) {
		super(p);
	}
}
