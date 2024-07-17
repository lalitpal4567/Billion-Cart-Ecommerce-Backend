package com.billioncart.exception;

public class CartItemNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 860938942314486995L;

	public CartItemNotFoundException() {}
	
	public CartItemNotFoundException(String message) {
		super(message);
	}
}
