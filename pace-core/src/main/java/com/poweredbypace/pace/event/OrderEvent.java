package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.order.Order;

public class OrderEvent extends ApplicationEvent {

	private Long orderId;
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public OrderEvent() { }

	public OrderEvent(Order order) {
		this.orderId = order.getId();
	}
}
