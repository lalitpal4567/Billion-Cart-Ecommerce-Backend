package com.billioncart.exception;

public class BrandNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3896245402792234791L;

	public BrandNotFoundException() {}
	
	public BrandNotFoundException(String message) {
		super(message);
	}
}
