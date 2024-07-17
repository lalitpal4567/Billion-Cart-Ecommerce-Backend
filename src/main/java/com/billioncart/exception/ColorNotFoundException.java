package com.billioncart.exception;

public class ColorNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -5791354537522868773L;

	public ColorNotFoundException() {}
	
	public ColorNotFoundException(String message) {
		super(message);
	}
}
