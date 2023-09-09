package me.triplesteak.metropolis.world;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.item.ItemFormat;
import me.triplesteak.metropolis.item.playerhead.PlayerHead;

public final class ATM {
	public static final String WITHDRAW_CASH = ChatColor.YELLOW + "Withdraw Cash";
	public static final String DEPOSIT_CASH = ChatColor.YELLOW + "Deposit Cash";
	public static final String DEPOSIT_CHEQUE = ChatColor.YELLOW + "Deposit Cheque";

	// item frames are initialized in ItemFrames file
	private static final HashMap<Bank, Integer> ATM_MAP_IDs = new HashMap<>();

	private static final HashMap<Bank, Inventory> INVENTORIES = new HashMap<>(); // ATM option selection

	/*
	 * ACTIVE_BANKS:			bank that owns ATM that player is using
	 * AWAITING_RESPONSE:		if the ATM is waiting for the player's text input
	 * ACTIVE_OPTIONS:			(1) withdraw cash
	 * 							(2) deposit cash
	 * 							(3) deposit cheque
	 * ACTIVE_ACCOUNT_CHOICE:	# of account with which the transaction will occur
	 */
	public static final HashMap<Player, Bank> ACTIVE_BANKS = new HashMap<>();
	public static final HashMap<Player, Boolean> AWAITING_RESPONSE = new HashMap<>();
	private static final HashMap<Player, Integer> ACTIVE_OPTIONS = new HashMap<>();
	private static final HashMap<Player, Integer> ACTIVE_ACCOUNT_CHOICE = new HashMap<>();

	static {
		ATM_MAP_IDs.put(Bank.TEEDEE_BANK, 16);

		ItemStack withdrawCash = PlayerHead.MONEY_STACK.getItemStack().clone();
		ItemStack depositCash = PlayerHead.MONEY_STACK.getItemStack().clone();
		ItemStack depositCheque = ArmorStandItems.getItemStack(Material.STONE_AXE, 1);

		ItemMeta meta1 = withdrawCash.getItemMeta();
		ItemMeta meta2 = depositCash.getItemMeta();
		ItemMeta meta3 = depositCheque.getItemMeta();

		meta1.setDisplayName(WITHDRAW_CASH);
		meta2.setDisplayName(DEPOSIT_CASH);
		meta3.setDisplayName(DEPOSIT_CHEQUE);

		meta1.setLore(Arrays.asList(ChatColor.GRAY + "Extract funds from an existing",
				ChatColor.GRAY + "bank account into your wallet."));
		meta2.setLore(Arrays.asList(ChatColor.GRAY + "Transfer funds from your wallet",
				ChatColor.GRAY + "to an existing bank account."));
		meta3.setLore(Arrays.asList(ChatColor.GRAY + "Consume a cheque and add the funds",
				ChatColor.GRAY + "to an existing bank account."));

		meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		meta3.setUnbreakable(true);
		meta3.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		withdrawCash.setItemMeta(meta1);
		depositCash.setItemMeta(meta2);
		depositCheque.setItemMeta(meta3);

		for (Bank bank : Bank.ALL_BANKS) {
			Inventory inv = Bukkit.createInventory(null, 9,
					(ChatColor.DARK_GREEN + bank.name + ChatColor.RESET + " ATM"));
			inv.setItem(2, withdrawCash);
			inv.setItem(4, depositCash);
			inv.setItem(6, depositCheque);
			INVENTORIES.put(bank, inv);
		}
	}

	public static boolean isATM(ItemFrame frame) {
		for (Bank bank : Bank.ALL_BANKS) {
			if (ATM_MAP_IDs.get(bank) == ItemFormat.getMapID(frame.getItem()))
				return true;
		}
		return false;
	}

	/**
	 * Opens ATM option selection screen
	 */
	public static void openATM(Player player, ItemFrame frame) {
		for (Bank bank : Bank.ALL_BANKS) {
			if (ATM_MAP_IDs.get(bank) == ItemFormat.getMapID(frame.getItem())) {
				player.openInventory(INVENTORIES.get(bank));
				ACTIVE_BANKS.put(player, bank);
				return;
			}
		}
	}

	/**
	 * Player chooses an option on the ATM
	 *
	 * (1) withdraw cash
	 * (2) deposit cash
	 * (3) deposit cheque
	 */
	public static void setActiveOption(Player player, int option) {
		ACTIVE_OPTIONS.put(player, option);
		player.closeInventory();
		ACTIVE_BANKS.get(player).showActiveAccounts(player, true);
	}

	/**
	 * Called after player selects an account (option already chosen)
	 */
	public static void selectAccount(Player player, int account) {
		ACTIVE_ACCOUNT_CHOICE.put(player, account);
		player.closeInventory();

		switch (ACTIVE_OPTIONS.get(player)) {
		case 1: // withdraw
			if (ACTIVE_BANKS.get(player).getAccountPlan(account).maxWithdrawals != -1
					&& ACTIVE_BANKS.get(player).WITHDRAWALS.get(player)
							.get(account) >= ACTIVE_BANKS.get(player).getAccountPlan(account).maxWithdrawals) {
				ATM.printMessage(player, "Maximum weekly withdrawals reached!");
				return;
			}

			ATM.printMessage(player, "Enter the amount you would like to withdraw:");
			AWAITING_RESPONSE.put(player, true);
			break;
		case 2: // deposit cash
			ATM.printMessage(player, "Enter the amount you would like to deposit:");
			AWAITING_RESPONSE.put(player, true);
			break;
		case 3: // deposit cheque
			// open inventory in which player can place checks
			break;
		}
	}

	public static void transferFunds(Player player, String response) {
		double funds;
		try {
			funds = Double.parseDouble(response);
		} catch (Exception ex) {
			ATM.printMessage(player, "Invalid format. Try again.");
			return;
		}
		
		switch (ACTIVE_OPTIONS.get(player)) {
		case 1: // withdraw
			ACTIVE_BANKS.get(player).withdraw(player, ACTIVE_ACCOUNT_CHOICE.get(player), funds);

			break;
		case 2: // deposit cash
			ACTIVE_BANKS.get(player).deposit(player, ACTIVE_ACCOUNT_CHOICE.get(player), funds);

			break;
		}
		
		AWAITING_RESPONSE.put(player, false);
	}

	public static void selectAccount(Player player, String accountName) {
		try {
			int number = Integer.parseInt(accountName.substring(accountName.indexOf('#') + 1));
			selectAccount(player, number - 1);
		} catch (Exception e) {
			// No action required
		}
	}

	public static void printMessage(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "[ATM] " + ChatColor.GREEN + message);
	}
}
