package com.billioncart.exception;

public class ProductOutOfStockException extends RuntimeException{
	public ProductOutOfStockException() {}
	
	public ProductOutOfStockException(String message) {
		super(message);
	}
}
