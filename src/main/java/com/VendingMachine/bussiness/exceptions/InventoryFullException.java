package com.VendingMachine.bussiness.exceptions;

public class InventoryFullException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public InventoryFullException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;

	}
}