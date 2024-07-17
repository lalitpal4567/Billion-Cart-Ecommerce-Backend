package com.billioncart.exception;

public class CarouselImageNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2699845678713525783L;

	public CarouselImageNotFoundException() {}
	
	public CarouselImageNotFoundException(String message) {
		super(message);
	}
}
