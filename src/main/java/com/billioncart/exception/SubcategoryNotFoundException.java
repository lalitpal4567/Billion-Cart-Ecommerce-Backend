package com.billioncart.exception;

public class SubcategoryNotFoundException extends RuntimeException{
	public SubcategoryNotFoundException() {}
	
	public SubcategoryNotFoundException(String message) {
		super(message);
	}
}
