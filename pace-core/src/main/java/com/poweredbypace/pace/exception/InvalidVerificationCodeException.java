package com.poweredbypace.pace.exception;

public class InvalidVerificationCodeException extends Exception {

	private static final long serialVersionUID = -3751141594847059987L;

	public InvalidVerificationCodeException() {
	}

	public InvalidVerificationCodeException(String message) {
		super(message);
	}

	public InvalidVerificationCodeException(Throwable cause) {
		super(cause);
	}

	public InvalidVerificationCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
