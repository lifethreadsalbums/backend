package com.poweredbypace.pace.exception;

public class EmailNotVerifiedException extends Exception {

	private static final long serialVersionUID = -3911402638510622483L;

	public EmailNotVerifiedException() {
		super();
	}

	public EmailNotVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailNotVerifiedException(String message) {
		super(message);
	}

	public EmailNotVerifiedException(Throwable cause) {
		super(cause);
	}

}
