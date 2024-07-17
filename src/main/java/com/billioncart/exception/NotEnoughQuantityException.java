package com.billioncart.exception;

public class NotEnoughQuantityException extends RuntimeException{
	public NotEnoughQuantityException() 	{}
	
	public NotEnoughQuantityException(String message) {
		super(message);
	}
}
