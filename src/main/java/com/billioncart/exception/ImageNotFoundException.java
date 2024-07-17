package com.billioncart.exception;

public class ImageNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -1246345952102440964L;

	public ImageNotFoundException() {}
	
	public ImageNotFoundException(String message) {
		super(message);
	}
}
