package com.VendingMachine.bussiness.model;

import java.util.List;

import com.VendingMachine.bussiness.exceptions.InventoryFullException;

public interface VendingMachine {

	public double selectItemAndGetPrice(Integer itemPlace);

	public void insertMoney(Coin money) throws InventoryFullException;

	public Collect<Items, List<Coin>> collectItemAndChange();

	public List<Coin> refund();

	public void reset();

	public void refill(int itemPlace, Object item, int quantity);

}