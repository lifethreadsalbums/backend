package com.poweredbypace.pace.exception;

@SuppressWarnings("serial")
public class PartialUpdateException extends RuntimeException {

	public PartialUpdateException() {
	}

	public PartialUpdateException(String arg0) {
		super(arg0);
	}

	public PartialUpdateException(Throwable arg0) {
		super(arg0);
	}

	public PartialUpdateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
