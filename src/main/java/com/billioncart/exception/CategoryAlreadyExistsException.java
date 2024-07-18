package com.billioncart.exception;

public class CategoryAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -7897328991009251438L;

	public CategoryAlreadyExistsException() {}
	
	public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}

