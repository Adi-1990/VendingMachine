package com.VendingMachine.bussiness.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory<T> {

	private Map<Integer, List<T>> inventory = new HashMap<Integer, List<T>>();

	public void clear() {
		inventory.clear();
	}

	public void add(int place, T item) {
		List<T> list = inventory.get(place);
		list.add(item);
	}

	public boolean hasItem(int itemCode) {
		if (inventory.get(itemCode).size() > 0) {
			return true;
		}
		return false;
	}

	public void deduct(int itemPlace) {
		if (hasItem(itemPlace)) {
			List<T> list = inventory.get(itemPlace);
			list.remove(inventory.get(itemPlace).size() - 1);
		}
	}

	public int getInventory(Integer itemPlace) {
		List<T> list = inventory.get(itemPlace);
		return list.size();
	}

	public List<T> findByPlace(int place) {
		List<T> list = new ArrayList<T>();
		for (T p : inventory.get(place)) {
			list.add(p);
		}
		return list;
	}

	public void refill(int itemPlace, T item, int quantity) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < quantity; i++) {
			list.add(item);
		}
		inventory.put(itemPlace, list);
	}

}