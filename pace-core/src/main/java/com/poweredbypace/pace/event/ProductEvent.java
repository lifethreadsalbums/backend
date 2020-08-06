package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;

public class ProductEvent extends ApplicationEvent {

	private Long productId;
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public ProductEvent() { }

	public ProductEvent(Product product) {
		this.productId = product.getId();
	}
}
