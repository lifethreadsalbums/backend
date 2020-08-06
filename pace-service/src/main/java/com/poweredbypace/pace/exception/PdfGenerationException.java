package com.poweredbypace.pace.exception;

public class PdfGenerationException extends RuntimeException {

	private static final long serialVersionUID = 2060874571592818082L;

	public PdfGenerationException() {
	}

	public PdfGenerationException(String arg0) {
		super(arg0);
	}

	public PdfGenerationException(Throwable arg0) {
		super(arg0);
	}

	public PdfGenerationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}