package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProoferEditsCompletedReminderEvent extends ProoferEvent {
	
	public ProoferEditsCompletedReminderEvent() { }

	public ProoferEditsCompletedReminderEvent(Product p) {
		super(p);
	}
}
