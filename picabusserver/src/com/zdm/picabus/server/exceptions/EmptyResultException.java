package com.zdm.picabus.server.exceptions;

/**
 * 
 * Custom exception representing that the result data set
 * for the desired query was empty
 *
 */
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
