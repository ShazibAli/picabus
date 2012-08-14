package com.zdm.picabus.server.exceptions;

public class EmptyResultException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = null;
	 
    public EmptyResultException() {
        super();
    }
 
    public EmptyResultException(String message) {
        super(message);
        this.message = message;
    }
 
    public EmptyResultException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
}
