package com.VendingMachine.bussiness.exceptions;

public class NotFullPaidException extends RuntimeException {

	private static final long serialVersionUID = 3757074500897299054L;
	private String message;
	private double remaining;

	public NotFullPaidException(String message, double remainingBalance) {
		this.message = message;
		this.remaining = remainingBalance;
	}

	public double getRemaining() {
		return remaining;
	}

	@Override
	public String getMessage() {
		return message + remaining;
	}

}