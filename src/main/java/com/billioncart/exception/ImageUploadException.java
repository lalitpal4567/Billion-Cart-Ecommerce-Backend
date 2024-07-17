package com.billioncart.exception;

public class ImageUploadException extends RuntimeException{

	private static final long serialVersionUID = -661947078479568200L;

	public ImageUploadException() {}
	
	public ImageUploadException(String message) {
		super(message);
	}
}
