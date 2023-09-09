package me.triplesteak.metropolis.savedata;

import java.io.File;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import me.triplesteak.metropolis.world.Bank;

public final class BankData {
	public static final String BANK_DIR = "bank";

	private static String getBankDirectory(Player player, Bank bank) {
		return PlayerDataHandler.getSubdirectory(player, BANK_DIR) + File.separator + bank.name + ".txt";
	}

	public static void loadBankData(Player player, Bank bank) {
		try {
			String string = FileUtils.readFileToString(new File(getBankDirectory(player, bank)),
					PlayerDataHandler.ENCODING);
			String[] list = string.split(" ");
			for (int i = 0; i < list.length; i += 3) {
				int planNum = Integer.parseInt(list[i * 3]);
				double balance = Double.parseDouble(list[i * 3 + 1]);
				int withdrawals = Integer.parseInt(list[i * 3 + 2]);

				bank.addAccount(player, planNum, balance, withdrawals);
			}
		} catch (Exception e) {

		}
	}

	public static void loadBankData(Player player) {
		for (Bank bank : Bank.ALL_BANKS)
			loadBankData(player, bank);
	}

	public static void saveBankData(Player player, Bank bank) {
		if (bank.ACTIVE_ACCOUNTS.containsKey(player)) {
			try {
				File f = new File(getBankDirectory(player, bank));
				f.getParentFile().mkdirs();
				f.createNewFile();
				String toWrite = "";

				for (int i = 0; i < bank.ACTIVE_ACCOUNTS.get(player).size(); i++) {
					toWrite += bank.ACTIVE_ACCOUNTS.get(player).get(i) + " " + bank.BALANCES.get(player).get(i) + " "
							+ bank.WITHDRAWALS.get(player).get(i);
				}

				FileUtils.writeStringToFile(f, toWrite, PlayerDataHandler.ENCODING);
			} catch (Exception e) {

			}
		}
	}

	public static void saveAll() {
		for (Bank bank : Bank.ALL_BANKS) {
			for (Player player : bank.ACTIVE_ACCOUNTS.keySet())
				saveBankData(player, bank);
		}
	}
}
