package com.poweredbypace.pace.exception;

public class ProductNameExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -2354727597489277169L;

	public ProductNameExistsException() {
		super();
	}

	public ProductNameExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ProductNameExistsException(String arg0) {
		super(arg0);
	}

	public ProductNameExistsException(Throwable arg0) {
		super(arg0);
	}

}
