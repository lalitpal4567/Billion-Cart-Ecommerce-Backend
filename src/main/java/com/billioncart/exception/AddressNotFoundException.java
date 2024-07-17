package com.billioncart.exception;

public class AddressNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 759316005783245843L;

	public AddressNotFoundException() {}
	
	public AddressNotFoundException(String message) {
		super(message);
	}
}
