package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.order.Order;


public class OrderCreatedEvent extends OrderEvent {

	public OrderCreatedEvent() { }

	public OrderCreatedEvent(Order order) {
		super(order);
	}
	
}
