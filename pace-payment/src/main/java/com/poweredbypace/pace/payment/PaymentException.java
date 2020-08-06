package com.poweredbypace.pace.payment;

public class PaymentException extends RuntimeException {

	private static final long serialVersionUID = -4787938410605585202L;

	public PaymentException() {
	}

	public PaymentException(String arg0) {
		super(arg0);
	}

	public PaymentException(Throwable arg0) {
		super(arg0);
	}

	public PaymentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
