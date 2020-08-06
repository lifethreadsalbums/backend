package com.poweredbypace.pace.exception;

import com.poweredbypace.pace.domain.Product;

public class LowResImagesException extends ProductNotDesignedException {
	
	private static final long serialVersionUID = -6961881769154495686L;
	
	public LowResImagesException() {
		super();
	}

	public LowResImagesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LowResImagesException(String arg0, Product product) {
		super(arg0, product);
	}

	public LowResImagesException(Throwable arg0) {
		super(arg0);
	}

}
