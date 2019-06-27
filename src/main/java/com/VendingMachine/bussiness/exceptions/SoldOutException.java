package com.VendingMachine.bussiness.exceptions;

public class SoldOutException extends RuntimeException {

	private static final long serialVersionUID = 2380249754709819369L;
	private String message;

	public SoldOutException(String string) {
		this.message = string;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
