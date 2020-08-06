package com.poweredbypace.pace.exception;

public class JsonException extends RuntimeException {

	private static final long serialVersionUID = -5072572644701815436L;

	public JsonException() {
	}

	public JsonException(String arg0) {
		super(arg0);
	}

	public JsonException(Throwable arg0) {
		super(arg0);
	}

	public JsonException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
