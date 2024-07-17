package com.billioncart.exception;

public class WishlistNotFoundException extends RuntimeException{
	public WishlistNotFoundException() {}
	
	public WishlistNotFoundException(String message) {
		super(message);
	}
}
