package com.billioncart.exception;

public class WishlistItemNotFoundException extends RuntimeException{
	public WishlistItemNotFoundException() {}
	
	public WishlistItemNotFoundException(String message) {
		super(message);
	}
}
