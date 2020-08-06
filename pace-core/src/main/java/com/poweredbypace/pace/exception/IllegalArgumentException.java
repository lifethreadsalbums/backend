package com.poweredbypace.pace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalArgumentException extends RuntimeException {
    
    private static final long serialVersionUID = -665473009191800102L;
	
    public IllegalArgumentException() {
        super();
    }
    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
    public IllegalArgumentException(String message) {
        super(message);
    }
    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }
}