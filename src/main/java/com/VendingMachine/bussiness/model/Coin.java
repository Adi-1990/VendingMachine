package com.VendingMachine.bussiness.model;

public enum Coin {

//	$10_BANI(0.1), $50_BANI(0.5), $1_LEU(1.0), $5_LEI(5.0);

//	$10_BANI(0.1, 50), $50_BANI(0.5, 50), $1_LEU(1.0, 20), $5_LEI(5.0, 15);

	$10_BANI(0.1, 6, 1001), $50_BANI(0.5, 6, 1050), $1_LEU(1.0, 6, 2001), $5_LEI(5.0, 6, 2005);

	private double denomination;
	int capacity;
	int place;

	private Coin(double denomination, int capacity, int place) {
		this.denomination = denomination;
		this.capacity = capacity;
		this.place = place;
	}

	public double getDenomination() {
		return denomination;
	}

	public int capacity() {
		return capacity;
	}

	public int place() {
		return place;
	}

}
