package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;


public class ProductPurchasedEvent extends ProductEvent {

	public ProductPurchasedEvent() { }

	public ProductPurchasedEvent(Product product) {
		super(product);
	}
	
}
