package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferEditsCompletedEvent extends ProoferEvent {
	
	public ProoferEditsCompletedEvent() { }

	public ProoferEditsCompletedEvent(Product p) {
		super(p);
	}
}
