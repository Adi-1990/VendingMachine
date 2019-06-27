package com.VendingMachine.bussiness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.VendingMachine.bussiness.exceptions.InventoryFullException;
import com.VendingMachine.bussiness.exceptions.NotFullPaidException;
import com.VendingMachine.bussiness.exceptions.NotSufficientChangeException;
import com.VendingMachine.bussiness.exceptions.SoldOutException;
import com.VendingMachine.bussiness.model.Coin;
import com.VendingMachine.bussiness.model.Collect;
import com.VendingMachine.bussiness.model.Inventory;
import com.VendingMachine.bussiness.model.Items;
import com.VendingMachine.bussiness.model.VendingMachine;

public class VendingMachineImpl implements VendingMachine {
	private Inventory<Coin> cashInventory = new Inventory<Coin>();
	private Inventory<Items> itemInventory = new Inventory<Items>();
	private double totalSales;
	private Items currentItem;
	private int currentItemPlace;
	private double currentBalance;

	public VendingMachineImpl() {
		initialize();
	}

	private void initialize() {
		cashInventory.refill(Coin.$10_BANI.place(), Coin.$10_BANI, Coin.$10_BANI.capacity() / 2);
		cashInventory.refill(Coin.$50_BANI.place(), Coin.$50_BANI, Coin.$50_BANI.capacity() / 2);
		cashInventory.refill(Coin.$1_LEU.place(), Coin.$1_LEU, Coin.$1_LEU.capacity() / 2);
		cashInventory.refill(Coin.$5_LEI.place(), Coin.$5_LEI, Coin.$5_LEI.capacity() / 2);

		itemInventory.refill(1, Items.COKE, 5);
		itemInventory.refill(2, Items.COKE, 5);
		itemInventory.refill(3, Items.PEPSI, 5);
		itemInventory.refill(4, Items.PEPSI, 5);
	}

//__________________________________________________________________________
//	@Override
	public double selectItemAndGetPrice(Integer itemPlace) {
		currentItemPlace = itemPlace;
		List<Items> findByPlace = itemInventory.findByPlace(itemPlace);
		if (!findByPlace.isEmpty()) {
			currentItem = findByPlace.get(findByPlace.size() - 1);
			return findByPlace.get(findByPlace.size() - 1).getPrice();
		}
		throw new SoldOutException("Sold Out! select another product");
	}

//__________________________________________________________________________
//	@Override
	List<Coin> cashDeposited = new ArrayList<Coin>();

	public void insertMoney(Coin money) throws InventoryFullException {
		if (cashInventory.getInventory(money.place()) > money.capacity()) {
			refund();
			updateCashInventory(cashDeposited);
			cashDeposited.clear();
			throw new InventoryFullException("Cash inventory for :" + money.getDenomination() + " lei is full");
		} else {
			cashDeposited.add(money);
			currentBalance = (currentBalance + money.getDenomination());
			cashInventory.add(money.place(), money);
		}
	}

//__________________________________________________________________________	
//	@Override
	public Collect<Items, List<Coin>> collectItemAndChange() {
		Items item = collectItem();
		totalSales = totalSales + currentItem.getPrice();
		List<Coin> change = collectChange();
		return new Collect<Items, List<Coin>>(item, change);
	}

	private Items collectItem() throws NotSufficientChangeException, NotFullPaidException {
		if (isFullPaid()) {
			if (hasSufficientChange()) {
				int position = currentItemPlace;
				itemInventory.deduct(position);
				return currentItem;
			}
			throw new NotSufficientChangeException("Not Sufficient change in Inventory");
		}
		double remainingBalance = currentItem.getPrice() - currentBalance;
		throw new NotFullPaidException("Price not full paid, remaining : ", remainingBalance);
	}

	private boolean isFullPaid() {
		if (currentBalance >= currentItem.getPrice()) {
			return true;
		}
		return false;
	}

	private boolean hasSufficientChange() {
		return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
	}

	private boolean hasSufficientChangeForAmount(double amount) {
		boolean hasChange = true;
		try {
			getChange(amount);
		} catch (NotSufficientChangeException nsce) {
			return hasChange = false;
		}

		return hasChange;
	}

	private List<Coin> collectChange() {
		double changeAmount = currentBalance - currentItem.getPrice();
		List<Coin> change = getChange(changeAmount);
		updateCashInventory(change);
		currentBalance = 0;
		currentItem = null;
		return change;
	}

	private List<Coin> getChange(double amount) throws NotSufficientChangeException {
		List<Coin> changes = Collections.emptyList();

		if (amount > 0) {
			changes = new ArrayList<Coin>();
			double balance = amount;
			while (balance > 0) {
				if (balance >= Coin.$5_LEI.getDenomination() && cashInventory.hasItem(Coin.$5_LEI.place())) {
					changes.add(Coin.$5_LEI);
					balance = balance - Coin.$5_LEI.getDenomination();
				} else if (balance >= Coin.$1_LEU.getDenomination() && cashInventory.hasItem(Coin.$1_LEU.place())) {
					changes.add(Coin.$1_LEU);
					balance = balance - Coin.$1_LEU.getDenomination();
				} else if (balance >= Coin.$50_BANI.getDenomination() && cashInventory.hasItem(Coin.$50_BANI.place())) {
					changes.add(Coin.$50_BANI);
					balance = balance - Coin.$50_BANI.getDenomination();
				} else if (balance >= Coin.$10_BANI.getDenomination() && cashInventory.hasItem(Coin.$10_BANI.place())) {
					changes.add(Coin.$10_BANI);
					balance = balance - Coin.$10_BANI.getDenomination();
				} else {
					throw new NotSufficientChangeException("NotSufficientChange, Please try another product");
				}
			}
		}
		return changes;
	}

	private void updateCashInventory(List<Coin> refund) {
		for (Coin c : refund) {
			cashInventory.deduct(c.place());
		}
	}

//_____________________________________________________________
//	@Override
	public List<Coin> refund() {
		List<Coin> refund = getChange(currentBalance);
		updateCashInventory(refund);
		currentBalance = 0;
		currentItem = null;
		return refund;
	}

//_____________________________________________________________
//	@Override
	public void reset() {
		cashInventory.clear();
		itemInventory.clear();
		totalSales = 0;
		currentItem = null;
		currentBalance = 0;
	}

	public void printStats() {
		System.out.println("Total Sales : " + totalSales);
		System.out.println("Current Item Inventory : " + itemInventory.getInventory(3));
		System.out.println("Current Cash 10 Bani: " + cashInventory.getInventory(Coin.$10_BANI.place()));
		System.out.println("Current Cash 50 Bani : " + cashInventory.getInventory(Coin.$50_BANI.place()));
		System.out.println("Current Cash 1 Leu : " + cashInventory.getInventory(Coin.$1_LEU.place()));
		System.out.println("Current Cash 5 Lei : " + cashInventory.getInventory(Coin.$5_LEI.place()));
	}

	public void refill(int itemPlace, Object item, int quantity) {
		refill(itemPlace, item, quantity);
	}

}