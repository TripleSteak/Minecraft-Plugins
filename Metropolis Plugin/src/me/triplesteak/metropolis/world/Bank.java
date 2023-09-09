package me.triplesteak.metropolis.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.item.ItemFormat;
import me.triplesteak.metropolis.item.Wallet;
import me.triplesteak.metropolis.item.playerhead.PlayerHead;
import me.triplesteak.metropolis.savedata.BankData;

public class Bank {
	public static final List<Bank> ALL_BANKS = new ArrayList<>();

	/*
	 * Number of accounts allowed per player
	 */
	private final int maxAccounts;

	/*
	 * Account plans currently used by the player
	 */
	private final List<AccountPlan> availablePlans = new ArrayList<>();

	/*
	 * Active player accounts, account plans are saved by ID # (position in availablePlans list)
	 */
	public final HashMap<Player, List<Integer>> ACTIVE_ACCOUNTS = new HashMap<>();
	public final HashMap<Player, List<Double>> BALANCES = new HashMap<>(); // List<Double> is list for each plan #
	public final HashMap<Player, List<Integer>> WITHDRAWALS = new HashMap<>();

	public static final Bank TEEDEE_BANK;

	public String name;

	private Inventory chequingPlans;
	private Inventory savingsPlans;

	static {
		TEEDEE_BANK = new Bank("TeeDee Bank", 3);
		TEEDEE_BANK.addAccountPlan(new AccountPlan(AccountPlan.CHEQUING, 0, -1, 0, 0, 800, "Eight and Under",
				"A convenient and pocket-friendly chequing account perfect for new TeeDee customers. Ease your way into eight and under!"));
		TEEDEE_BANK.makePlans();
	}

	public Bank(String name, int maxAccounts) {
		this.name = name;
		this.maxAccounts = maxAccounts;

		ALL_BANKS.add(this);
	}

	public static void weeklyReset() {
		for (Bank bank : ALL_BANKS) {
			for (Player player : bank.WITHDRAWALS.keySet()) {
				for (int i = 0; i < bank.WITHDRAWALS.get(player).size(); i++) {
					bank.WITHDRAWALS.get(player).set(i, 0);
				}
			}
		}
		BankData.saveAll();
	}

	private void setBalance(Player player, int planNum, double newBalance) {
		BALANCES.get(player).set(planNum, newBalance); // readjust balance
	}

	/**
	 * 
	 * @return  if the withdrawal is possible
	 */
	public boolean withdraw(Player player, int planNum, double withdrawal) {
		double walletSpaceLeft = Wallet.totalWalletCapacity(player) - Wallet.totalWalletFunds(player);
		if (withdrawal > walletSpaceLeft) {
			ATM.printMessage(player, "Insufficient wallet capacity!");
			return false;
		}

		double minWithdrawal = 0;
		double maxWithdrawal = BALANCES.get(player).get(planNum);

		if (withdrawal <= minWithdrawal) {
			ATM.printMessage(player, "A non-positive withdrawal cannot be made.");
			return false;
		}
		if (withdrawal > maxWithdrawal) {
			ATM.printMessage(player, "There aren't that many funds in your account.");
			return false;
		}

		Wallet.addFunds(player, withdrawal);
		setBalance(player, planNum, BALANCES.get(player).get(planNum) - withdrawal);
		WITHDRAWALS.get(player).set(planNum, WITHDRAWALS.get(player).get(planNum) + 1);
		BankData.saveBankData(player, this);

		ATM.printMessage(player, "You have successfully withdrawed $" + ChatColor.BOLD + withdrawal + ChatColor.RESET
				+ ChatColor.GREEN + ".");

		return true;
	}

	/**
	 * 
	 * @return	if the deposit is possible
	 */
	public boolean deposit(Player player, int planNum, double deposit) {
		if (deposit > Wallet.totalWalletFunds(player)) {
			ATM.printMessage(player, "Insufficient funds for deposit!");
			return false;
		}

		double minDeposit = 0;
		double maxDeposit = getAccountPlan(planNum).capacity - BALANCES.get(player).get(planNum);

		if (deposit <= minDeposit) {
			ATM.printMessage(player, "A non-positive deposit cannot be made.");
			return false;
		}
		if (deposit > maxDeposit) {
			ATM.printMessage(player, "Your account does not have sufficient capacity for that deposit.");
			return false;
		}

		Wallet.removeFunds(player, deposit);
		setBalance(player, planNum, BALANCES.get(player).get(planNum) + deposit);
		BankData.saveBankData(player, this);

		ATM.printMessage(player, "You have successfully deposited $" + ChatColor.BOLD + deposit + ChatColor.RESET
				+ ChatColor.GREEN + ".");

		return true;
	}

	public void makePlans() {
		chequingPlans = makeAvailablePlans(AccountPlan.CHEQUING);
		savingsPlans = makeAvailablePlans(AccountPlan.SAVINGS);
	}

	public int getMaxAccounts() {
		return maxAccounts;
	}

	private void addAccountPlan(AccountPlan plan) {
		availablePlans.add(plan);
	}

	public AccountPlan getAccountPlan(int ID) {
		return availablePlans.get(ID);
	}

	public Inventory getAvailablePlans(int planType) {
		return planType == AccountPlan.CHEQUING ? chequingPlans : savingsPlans;
	}

	/**
	 * Adds a new bank account for the given player.
	 */
	public void addAccount(Player player, AccountPlan plan) {
		addAccount(player, plan, 0.0, 0);
	}

	public void addAccount(Player player, int planNum, double balance, int withdrawals) {
		addAccount(player, availablePlans.get(planNum), balance, withdrawals);
	}

	public void addAccount(Player player, AccountPlan plan, double balance, int withdrawals) {
		if (!ACTIVE_ACCOUNTS.containsKey(player))
			ACTIVE_ACCOUNTS.put(player, new ArrayList<>());
		if (!BALANCES.containsKey(player))
			BALANCES.put(player, new ArrayList<>());
		if (!WITHDRAWALS.containsKey(player))
			WITHDRAWALS.put(player, new ArrayList<>());

		if (ACTIVE_ACCOUNTS.get(player).size() < maxAccounts) {
			ACTIVE_ACCOUNTS.get(player).add(availablePlans.indexOf(plan));
			BALANCES.get(player).add(balance);
			WITHDRAWALS.get(player).add(withdrawals);
		}
	}

	/**
	 * Attempts to delete a player's bank account
	 */
	public void deleteAccount(Player player, int ID) {
		if (!ACTIVE_ACCOUNTS.containsKey(player) || ACTIVE_ACCOUNTS.get(player).size() <= ID)
			return;
		if (!BALANCES.containsKey(player) || BALANCES.get(player).get(ID) != 0)
			return;
		if (!WITHDRAWALS.containsKey(player))
			return;

		ACTIVE_ACCOUNTS.get(player).remove(ID);
		BALANCES.get(player).remove(ID);
		WITHDRAWALS.get(player).remove(ID);
	}

	public void showActiveAccounts(Player player, boolean atATM) {
		Inventory inv = Bukkit.createInventory(null, Math.max((maxAccounts / 4) * 9, 9), (ChatColor.DARK_GREEN
				+ this.name + ChatColor.RESET + (atATM ? " Account Selection" : " Active Accounts")));

		int activeIndex = 0, curRow = 0;
		int plansLeft = maxAccounts;

		while (plansLeft > 0) {
			int offset = 0;
			if (plansLeft < 4)
				offset += 4 - plansLeft;
			plansLeft -= 4;

			for (int i = 0; i < 4 && activeIndex < maxAccounts; i++) {
				AccountPlan plan = (ACTIVE_ACCOUNTS.containsKey(player)
						&& ACTIVE_ACCOUNTS.get(player).size() > activeIndex
								? availablePlans.get(ACTIVE_ACCOUNTS.get(player).get(activeIndex))
								: null);

				ItemStack is = plan == null ? new ItemStack(Material.BLACK_STAINED_GLASS_PANE)
						: new ItemStack(PlayerHead.MONEY_BAG.getItemStack());
				ItemMeta meta = is.getItemMeta();
				List<String> lore = new ArrayList<String>();

				int numWithdrawals = plan == null ? 0 : WITHDRAWALS.get(player).get(activeIndex);

				if (plan != null) {
					meta.setDisplayName(ChatColor.YELLOW + "Account #" + (1 + activeIndex));

					lore.add(ChatColor.GRAY + (plan.accountType == AccountPlan.CHEQUING ? "Chequing" : "Savings")
							+ " Account Plan");
					lore.add("");
					lore.add(ChatColor.WHITE + "Interest rate: " + ChatColor.YELLOW + plan.interestRate
							+ ChatColor.WHITE + "%" + ChatColor.GRAY + " per week");
					lore.add(ChatColor.WHITE + "Weekly withdrawals used: " + ChatColor.YELLOW
							+ (plan.maxWithdrawals == -1 ? "–"
									: (((double) numWithdrawals) / ((double) plan.maxWithdrawals) > 0.90 ? ChatColor.RED
											: ChatColor.YELLOW) + "" + numWithdrawals + ChatColor.GRAY + "/"
											+ plan.maxWithdrawals));
					lore.add("");
					lore.add(ChatColor.WHITE + "Withdrawal Cost: $" + ChatColor.YELLOW
							+ String.format("%.2f", plan.withdrawalCost));
					lore.add(ChatColor.WHITE + "Capacity: $" + ChatColor.YELLOW + String.format("%.2f", plan.capacity));
					lore.add("");
					lore.add(ChatColor.YELLOW + "Current account balance: $" + ChatColor.GOLD + "" + ChatColor.BOLD
							+ String.format("%.2f", BALANCES.get(player).get(activeIndex)));
				} else {
					meta.setDisplayName(ChatColor.RED + "Empty Account Slot");

					lore.add(ChatColor.GRAY + "Speak with a teller to add an account.");
				}

				meta.setLore(lore);
				is.setItemMeta(meta);

				inv.setItem(curRow * 9 + 1 + offset + 2 * i, is);

				activeIndex++;
			}
			curRow++;
		}

		player.openInventory(inv);
	}

	/**
	 * 
	 * @param planType chequing or savings
	 */
	private Inventory makeAvailablePlans(int planType) {
		int totalPlans = 0;
		for (AccountPlan plan : availablePlans)
			if (plan.accountType == planType)
				totalPlans++;
		int plansLeft = totalPlans;

		Inventory inv = Bukkit.createInventory(null, Math.max((totalPlans / 4) * 9, 9),
				(ChatColor.DARK_GREEN + this.name + ChatColor.RESET + " Account Plans"));

		int activeIndex = 0, curRow = 0;

		while (plansLeft > 0) {
			int offset = 0;
			if (plansLeft < 4)
				offset += 4 - plansLeft;
			plansLeft -= 4;

			for (int i = 0; i < 4 && activeIndex < totalPlans; i++) {
				while (availablePlans.get(activeIndex).accountType != planType)
					activeIndex++;

				AccountPlan plan = availablePlans.get(activeIndex);

				ItemStack is = new ItemStack(PlayerHead.MONEY_BAG.getItemStack());
				ItemMeta meta = is.getItemMeta();
				meta.setDisplayName(ChatColor.YELLOW + plan.planName);

				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.WHITE + (planType == AccountPlan.CHEQUING ? "Chequing" : "Savings")
						+ " Account Plan");
				lore.add("");
				for (String str : ItemFormat.formatDescription(plan.planDesc))
					lore.add(str);
				lore.add(ChatColor.WHITE + "Interest rate: " + ChatColor.YELLOW + plan.interestRate + ChatColor.WHITE
						+ "%" + ChatColor.GRAY + " per week");
				lore.add(ChatColor.WHITE + "Maximimum weekly withdrawals: " + ChatColor.YELLOW
						+ (plan.maxWithdrawals == -1 ? "Unlimited" : plan.maxWithdrawals));
				lore.add("");
				lore.add(ChatColor.WHITE + "Open Cost: $" + ChatColor.YELLOW + String.format("%.2f", plan.openCost));
				lore.add(ChatColor.WHITE + "Withdrawal Cost: $" + ChatColor.YELLOW
						+ String.format("%.2f", plan.withdrawalCost));
				lore.add(ChatColor.WHITE + "Capacity: $" + ChatColor.YELLOW + String.format("%.2f", plan.capacity));
				lore.add("");
				lore.add(ChatColor.YELLOW + "Click to open new account!");

				meta.setLore(lore);
				is.setItemMeta(meta);

				inv.setItem(curRow * 9 + 1 + offset + 2 * i, is);

				activeIndex++;
			}
			curRow++;
		}

		return inv;
	}

	public static class AccountPlan {
		public static final int CHEQUING = 14;
		public static final int SAVINGS = 15;

		public final int accountType; // chequing or savings

		public final double interestRate; // compound interest %/week, compounded daily
		public final int maxWithdrawals; // maximum # of withdrawals/week, -1 for unlimited

		public final double openCost; // cost to open the account
		public final double withdrawalCost; // cost per withdrawal
		public final double capacity; // maximum amount of money allowed

		public String planName;
		public String planDesc;

		/**
		 * For simplicity...
		 * 
		 * Only savings accounts may have a limit to weekly withdrawals Only savings
		 * accounts may generate interest
		 */
		public AccountPlan(int accountType, double interestRate, int maxWithdrawals, double openCost,
				double withdrawalCost, double capacity, String planName, String planDesc) {
			this.accountType = accountType;
			this.interestRate = interestRate;
			this.maxWithdrawals = maxWithdrawals;
			this.openCost = openCost;
			this.withdrawalCost = withdrawalCost;
			this.capacity = capacity;
			this.planName = planName;
			this.planDesc = planDesc;
		}
	}
}
