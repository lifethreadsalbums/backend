package com.poweredbypace.pace.exception;

public class EmailAlreadyVerifiedException extends Exception {

	private static final long serialVersionUID = -6015236100102206769L;

	public EmailAlreadyVerifiedException() {
		super();
	}

	public EmailAlreadyVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailAlreadyVerifiedException(String message) {
		super(message);
	}

	public EmailAlreadyVerifiedException(Throwable cause) {
		super(cause);
	}

}
