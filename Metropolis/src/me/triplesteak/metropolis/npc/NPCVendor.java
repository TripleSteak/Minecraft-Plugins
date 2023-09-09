package me.triplesteak.metropolis.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.item.ItemFormat;
import me.triplesteak.metropolis.item.Wallet;

public class NPCVendor extends NPC {
	/*
	 * Keeps track of most recently visited vendor per player
	 */
	public static final HashMap<Player, NPCVendor> LAST_VENDOR = new HashMap<>();

	public final List<Player> GREETED_PLAYERS = new ArrayList<>(); // players who have already received advertisement

	private String inventoryName;
	private int inventoryRows;

	private final List<Integer> itemIndices = new ArrayList<>(); // position of items in inventory
	private final List<List<ItemStack>> displayItems = new ArrayList<>(); // affordable, non-affordable
	private final List<List<ItemStack>> sellItems = new ArrayList<>();
	private final List<Double> prices = new ArrayList<>(); // prices, for simplicity

	public String greeting = "";
	public String saleComplete = "";
	public String tooExpensive = "";

	public NPCVendor(String name, Type type, Profession profession, Location loc, String inventoryName,
			int inventoryRows) { // remember to populate inventory and set dialogue afterwards
		super(name, type, profession, loc, true);

		this.inventoryName = inventoryName;
		this.inventoryRows = inventoryRows;

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() { // greetings timer
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (GREETED_PLAYERS.contains(p))
						continue;
					if (p.getWorld() == loc.getWorld()) { // player is in the same world
						if (loc.distance(p.getLocation()) < 6) { // player is less than 6 blocks away
							broadcastMessage(p, greeting);
							GREETED_PLAYERS.add(p);
						}
					}
				}
			}
		}, (long) (300 + Math.random() * 900), 100);
	}

	public String getInventoryName() {
		return inventoryName;
	}

	/**
	 * 
	 * @param pos          position in inventory
	 * @param displayItem  custom display item, mostly for combos
	 * @param sellItemList actual sell item
	 */
	private void addToInventory(int pos, List<ItemStack> displayItem, List<ItemStack> sellItemList, double price) {
		itemIndices.add(pos);
		displayItems.add(displayItem);
		sellItems.add(sellItemList);
		prices.add(price);
	}

	/**
	 * 
	 * @param pos      position in inventory
	 * @param price    price of sale
	 * @param sellItem item sold
	 */
	public void addToInventory(int pos, double price, ItemStack sellItem) {
		addToInventory(pos, ItemFormat.getVendorSellItem(sellItem, price), Arrays.asList(sellItem), price);
	}

	/**
	 * 
	 * @param pos          position in inventory
	 * @param name         name of combo
	 * @param description  description of combo
	 * @param price        price of combo
	 * @param sellItemList actual sell items
	 */
	public void addComboToInventory(int pos, String name, String description, double price,
			List<ItemStack> sellItemList) {
		addToInventory(pos, ItemFormat.getVendorSellCombo(sellItemList, name, description, price), sellItemList, price);
	}

	public void openInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, inventoryRows * 9, inventoryName);
		double funds = Wallet.totalWalletFunds(player);

		for (int i = 0; i < itemIndices.size(); i++) {
			if (prices.get(i) <= funds)
				inv.setItem(itemIndices.get(i), displayItems.get(i).get(0));
			else
				inv.setItem(itemIndices.get(i), displayItems.get(i).get(1));
		}

		player.openInventory(inv);

		LAST_VENDOR.put(player, this);
	}

	/**
	 * Called when player clicks a slot in a vendor's inventory
	 */
	public void clickSlot(Player player, int slot) {
		double funds = Wallet.totalWalletFunds(player);
		int index = -1;
		for (int i = 0; i < itemIndices.size(); i++)
			if (itemIndices.get(i) == slot)
				index = i;

		if (index == -1)
			return;

		if (funds >= prices.get(index)) { // can afford
			player.closeInventory();
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			broadcastMessage(player, saleComplete);
			Wallet.removeFunds(player, prices.get(index));

			for (ItemStack is : sellItems.get(index)) {
				if (player.getInventory().firstEmpty() == -1)
					player.getWorld().dropItem(player.getLocation(), is);
				else
					player.getInventory().addItem(is);
			}
		} else { // insufficient funds
			player.closeInventory();
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 3f, 1f);
			broadcastMessage(player, tooExpensive);
		}
	}

	@Override
	public void greet(Player player) {
		if (!GREETED_PLAYERS.contains(player)) { // not greeted yet, to avoid annoyance
			broadcastMessage(player, greeting);
			GREETED_PLAYERS.add(player);
		}
	}
}
