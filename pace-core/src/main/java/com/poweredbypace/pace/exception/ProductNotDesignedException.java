package com.poweredbypace.pace.exception;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;

public class ProductNotDesignedException extends RuntimeException {
	
	private static final long serialVersionUID = -2354727597489277169L;
	
	private Long productId;
	private ProductType productType;
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public ProductNotDesignedException() {
		super();
	}

	public ProductNotDesignedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ProductNotDesignedException(String arg0, Product product) {
		super(arg0);
		this.productId = product.getId();
		this.productType = product.getPrototypeProduct().getProductType();
	}

	public ProductNotDesignedException(Throwable arg0) {
		super(arg0);
	}

}
