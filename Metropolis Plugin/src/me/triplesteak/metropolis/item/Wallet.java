package me.triplesteak.metropolis.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class Wallet {
	private static final String WALLET_STRING = ChatColor.DARK_GRAY + "Wallet";

	private static final HashMap<ItemRarity, Double> WALLET_CAPACITIES = new HashMap<>();
	static {
		WALLET_CAPACITIES.put(ItemRarity.COMMON, 20.0);
		WALLET_CAPACITIES.put(ItemRarity.UNCOMMON, 200.0);
		WALLET_CAPACITIES.put(ItemRarity.FINE, 2000.0);
		WALLET_CAPACITIES.put(ItemRarity.RARE, 20000.0);
		WALLET_CAPACITIES.put(ItemRarity.EPIC, 200000.0);
		WALLET_CAPACITIES.put(ItemRarity.LEGENDARY, 2000000.0);
		WALLET_CAPACITIES.put(ItemRarity.MYTHICAL, 20000000.0);
	}

	/*
	 * List of uncommon wallets
	 */
	public static final ItemStack CREEPER_WALLET = makeWallet("Creeper Wallet", ItemRarity.UNCOMMON,
			Color.fromRGB(72, 196, 48));

	public static ItemStack makeWallet(String name, ItemRarity rarity, Color colour, double funds) {
		ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		double capacity = WALLET_CAPACITIES.get(rarity);

		((Damageable) meta).setDamage(1);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		meta.setDisplayName(rarity.getPrefix() + "" + ChatColor.BOLD + name);
		meta.setColor(colour);

		List<String> lore = new ArrayList<>();
		lore.add(WALLET_STRING);
		lore.add("");
		lore.add(rarity.getPrefix() + ">> " + ChatColor.GRAY + "Contains: $" + ChatColor.YELLOW
				+ String.format("%.2f", funds));
		lore.add(rarity.getPrefix() + ">> " + ChatColor.GRAY + "Capacity: $" + ChatColor.DARK_GRAY
				+ String.format("%.2f", capacity));
		lore.add("");
		lore.add(rarity.getEnvelope());

		meta.setLore(lore);
		is.setItemMeta(meta);
		return is;
	}

	public static ItemStack makeWallet(String name, ItemRarity rarity, Color colour) {
		return makeWallet(name, rarity, colour, 0.0);
	}

	public static boolean isWallet(ItemStack is) {
		return (is != null && ItemFormat.hasLore(is)
				&& is.getItemMeta().getLore().get(0).equalsIgnoreCase(WALLET_STRING));
	}

	public static double checkFunds(ItemStack is) {
		if (!isWallet(is))
			return 0;
		return Double.parseDouble(
				is.getItemMeta().getLore().get(2).substring(is.getItemMeta().getLore().get(2).indexOf('$') + 3));
	}

	public static double checkCapacity(ItemStack is) {
		if (!isWallet(is))
			return 0;
		return Double.parseDouble(
				is.getItemMeta().getLore().get(3).substring(is.getItemMeta().getLore().get(3).indexOf('$') + 3));
	}

	/**
	 * Ensure item stack is a wallet before calling
	 */
	public static void setFunds(ItemStack is, double funds) {
		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(2, lore.get(2).substring(0, 5) + ChatColor.GRAY + "Contains: $" + ChatColor.YELLOW
				+ String.format("%.2f", funds));
		meta.setLore(lore);
		is.setItemMeta(meta);
	}

	public static double totalWalletFunds(Player player) {
		double sum = 0.0;
		for (ItemStack is : player.getInventory())
			sum += checkFunds(is);
		return sum;
	}

	public static double totalWalletCapacity(Player player) {
		double sum = 0.0;
		for (ItemStack is : player.getInventory())
			sum += checkCapacity(is);
		return sum;
	}
	
	public static void addFunds(Player player, double value) {
		for (ItemStack is : player.getInventory()) {
			double spaceLeft = checkCapacity(is) - checkFunds(is);
			if (spaceLeft > 0) {
				if (value > spaceLeft) {
					value -= spaceLeft;
					setFunds(is, checkCapacity(is));
				} else if (value <= spaceLeft) {
					setFunds(is, checkFunds(is) + value);
					return;
				}
			}
		}
	}

	public static void removeFunds(Player player, double value) {
		for (ItemStack is : player.getInventory()) {
			double funds = checkFunds(is);
			if (funds != 0) {
				if (value > funds) {
					value -= funds;
					setFunds(is, 0);
				} else if (value <= funds) {
					setFunds(is, funds - value);
					return;
				}
			}
		}
	}
}
