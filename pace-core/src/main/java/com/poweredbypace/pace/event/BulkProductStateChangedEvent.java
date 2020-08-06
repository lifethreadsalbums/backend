package com.poweredbypace.pace.event;

import java.util.ArrayList;
import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;


public class BulkProductStateChangedEvent extends ApplicationEvent {
	
	private ProductState state;
	private List<Long> productIds;
	
	public void setState(ProductState state) {
		this.state = state;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	public ProductState getState() {
		return state;
	}

	public List<Long> getProductIds() {
		return productIds;
	}

	public BulkProductStateChangedEvent() { }

	public BulkProductStateChangedEvent(List<Product> products, ProductState state) {
		this.state = state;
		this.productIds = new ArrayList<Long>();
		for(Product p:products) {
			this.productIds.add(p.getId());
		}
	}
	
}
