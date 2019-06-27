package com.VendingMachine.bussiness.DALTests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.VendingMachine.bussiness.model.Coin;

public class $_10_Bani_DAO {

	public List<Coin> loadCoins(String path) {

		Path filePath = Paths.get(path);

		try {
			List<String> content = Files.readAllLines(filePath);
			List<Coin> coin = parseContent(content);
			return coin;

		} catch (IOException e) {
			System.err.println("The file with customers could not be found");
			return Collections.emptyList();
		}
	}

	private List<Coin> parseContent(List<String> allLines) {
		List<Coin> coin = new ArrayList<Coin>();

		for (String line : allLines) {
			String[] values = line.split(",");
			double value = Double.parseDouble(values[0]);
			int capacity = Integer.parseInt(values[1]);
			int place = Integer.parseInt(values[2]);
//			Coin coin2 = new Coin ;

//			coin.add(new Coin(value, capacity));
		}
		return coin;
	}

	public void saveCoins(String path, List<Coin> clients) {

		Path filePath = Paths.get(path);
		List<String> content = new ArrayList<String>();

		for (Coin c : clients) {
			content.add(c.toString());
		}
		try {
			Files.write(filePath, content);
		} catch (IOException e) {
			System.err.println("The list of customers could not be saved on disk!");
		}
	}
}
