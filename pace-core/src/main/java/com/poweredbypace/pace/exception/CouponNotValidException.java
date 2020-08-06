package com.poweredbypace.pace.exception;

public class CouponNotValidException extends RuntimeException {

	private static final long serialVersionUID = -2155598795472834280L;

	public CouponNotValidException() {
		super();
	}

	public CouponNotValidException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponNotValidException(String message) {
		super(message);
	}

	public CouponNotValidException(Throwable cause) {
		super(cause);
	}
	
}
