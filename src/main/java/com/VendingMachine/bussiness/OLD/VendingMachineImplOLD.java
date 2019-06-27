package com.VendingMachine.bussiness.OLD;

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

public class VendingMachineImplOLD implements VendingMachine {
	private Inventory<Coin> cashInventory = new Inventory<Coin>();
	private Inventory<Items> itemInventory = new Inventory<Items>();
	private double totalSales;
	private Items currentItem;
	private int currentItemPlace;
	private double currentBalance;
	private final int $10_BANI_PLACE = 1001;
	private final int $50_BANI_PLACE = 1050;
	private final int $1_LEU_PLACE = 2001;
	private final int $5_LEI_PLACE = 2005;

	public VendingMachineImplOLD() {
		initialize();
	}

	private void initialize() {

		cashInventory.refill($10_BANI_PLACE, Coin.$10_BANI, 5);
		cashInventory.refill($50_BANI_PLACE, Coin.$50_BANI, 5);
		cashInventory.refill($1_LEU_PLACE, Coin.$1_LEU, 5);
		cashInventory.refill($5_LEI_PLACE, Coin.$5_LEI, 5);

		itemInventory.refill(1, Items.COKE, 5);
		itemInventory.refill(2, Items.COKE, 5);
		itemInventory.refill(3, Items.PEPSI, 5);
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
	public void insertMoney(Coin money) {

		if (cashInventory.getInventory(findPlaceByValue(money)) > money.capacity()) {
			try {
				throw new InventoryFullException("Cash inventory for :" + money.getDenomination() + "is full");
			} catch (InventoryFullException e) {
				e.printStackTrace();
			}
		} else {

			currentBalance = (currentBalance + money.getDenomination());
			int place = findPlaceByValue(money);
			cashInventory.add(place, money);
		}
	}

	private int findPlaceByValue(Coin money) {
		int place = -1;
		switch (money) {
		case $10_BANI:
			place = $10_BANI_PLACE;
			break;
		case $50_BANI:
			place = $50_BANI_PLACE;
			break;
		case $1_LEU:
			place = $1_LEU_PLACE;
			break;
		case $5_LEI:
			place = $5_LEI_PLACE;
			break;
		default:
			return place;
		}
		return place;
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
				if (balance >= Coin.$5_LEI.getDenomination() && cashInventory.hasItem(findPlaceByValue(Coin.$5_LEI))) {
					changes.add(Coin.$5_LEI);
					balance = balance - Coin.$5_LEI.getDenomination();

				} else if (balance >= Coin.$1_LEU.getDenomination()
						&& cashInventory.hasItem(findPlaceByValue(Coin.$1_LEU))) {
					changes.add(Coin.$1_LEU);
					balance = balance - Coin.$1_LEU.getDenomination();

				} else if (balance >= Coin.$50_BANI.getDenomination()
						&& cashInventory.hasItem(findPlaceByValue(Coin.$50_BANI))) {
					changes.add(Coin.$50_BANI);
					balance = balance - Coin.$50_BANI.getDenomination();

				} else if (balance >= Coin.$10_BANI.getDenomination()
						&& cashInventory.hasItem(findPlaceByValue(Coin.$10_BANI))) {
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
			int place = findPlaceByValue(c);
			cashInventory.deduct(place);
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
		System.out.println("Current Cash 10 Bani: " + cashInventory.getInventory($10_BANI_PLACE));
		System.out.println("Current Cash 50 Bani : " + cashInventory.getInventory($50_BANI_PLACE));
		System.out.println("Current Cash 1 Leu : " + cashInventory.getInventory($1_LEU_PLACE));
		System.out.println("Current Cash 5 Lei : " + cashInventory.getInventory($5_LEI_PLACE));
	}

	public void refill(int itemPlace, Object item, int quantity) {
		refill(itemPlace, item, quantity);
	}
}