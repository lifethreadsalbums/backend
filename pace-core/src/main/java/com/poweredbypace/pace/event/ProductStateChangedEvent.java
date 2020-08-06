package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;


public class ProductStateChangedEvent extends ProductEvent {
	
	private ProductState previousState;
	
	public ProductState getPreviousState() {
		return previousState;
	}

	public void setPreviousState(ProductState previousState) {
		this.previousState = previousState;
	}

	public ProductStateChangedEvent() { }

	public ProductStateChangedEvent(Product product, ProductState previousState) {
		super(product);
		this.setPreviousState(previousState);
	}
	
}
