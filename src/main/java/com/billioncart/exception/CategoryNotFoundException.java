package com.billioncart.exception;

public class CategoryNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -5632186121770259911L;

	public CategoryNotFoundException(){}
	
	public CategoryNotFoundException(String message) {
		super(message);
	}
}
