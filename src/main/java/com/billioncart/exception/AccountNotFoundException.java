package com.billioncart.exception;

public class AccountNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1104038153262570290L;

	public AccountNotFoundException() {}
	
	public AccountNotFoundException(String message) {
		super(message);
	}
}
