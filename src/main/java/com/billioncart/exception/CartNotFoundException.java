package com.billioncart.exception;

public class CartNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 2663238739955178499L;
	
	public CartNotFoundException() {}
	public CartNotFoundException(String message) {
		super(message);
	}
}
