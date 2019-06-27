package com.VendingMachine.bussiness.model;

public enum Items {
	COKE("Coke", 1.0), PEPSI("Pepsi", 2.0), SODA("Soda", 1.5);

	private String title;
	private double price;

	private Items(String title, double price) {
		this.title = title;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return title;
	}

}
