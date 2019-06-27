package com.VendingMachine;

import com.VendingMachine.bussiness.VendingMachineImpl;
import com.VendingMachine.bussiness.exceptions.InventoryFullException;
import com.VendingMachine.bussiness.model.Coin;

public class Main {
	public static void main(String[] args) throws InventoryFullException {

		VendingMachineImpl machine1 = new VendingMachineImpl();
		machine1.printStats();
		machine1.selectItemAndGetPrice(3);
		machine1.insertMoney(Coin.$50_BANI);
		machine1.insertMoney(Coin.$50_BANI);
//		machine1.insertMoney(Coin.$50_BANI);
//		machine1.insertMoney(Coin.$50_BANI);
		machine1.insertMoney(Coin.$50_BANI);
		machine1.insertMoney(Coin.$5_LEI);
		machine1.collectItemAndChange();
//		machine1.refund();
		machine1.printStats();

	}
}