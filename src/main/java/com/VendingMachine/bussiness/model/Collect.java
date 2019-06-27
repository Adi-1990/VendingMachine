package com.VendingMachine.bussiness.model;

public class Collect<T1, T2> {

	private T1 first;
	private T2 second;

	public Collect(T1 first, T2 second) {
		super();
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

}
