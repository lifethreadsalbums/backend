package com.poweredbypace.pace.exception;

public class EmailAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2155598795472834280L;

	public EmailAlreadyExistsException() {
		super();
	}

	public EmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	public EmailAlreadyExistsException(Throwable cause) {
		super(cause);
	}
	
}
