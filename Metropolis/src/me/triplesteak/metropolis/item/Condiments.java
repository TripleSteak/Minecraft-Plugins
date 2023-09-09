package me.triplesteak.metropolis.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.triplesteak.metropolis.util.LocationUtils;

public final class Condiments {
	public static final List<ItemStack> AVAILABLE_FOODS = new ArrayList<>();

	/*
	 * List of locations with mustard/ketchup dispensers, should be tripwire hooks
	 */
	public static final List<Vector> KETCHUP_LOCATIONS = new ArrayList<>();
	public static final List<Vector> MUSTARD_LOCATIONS = new ArrayList<>();

	static {
		AVAILABLE_FOODS.add(Food.CLASSIC_HOT_DOG);
		AVAILABLE_FOODS.add(Food.MEXICAN_HOT_DOG);

		KETCHUP_LOCATIONS.add(new Vector(-77, 64, 486));

		MUSTARD_LOCATIONS.add(new Vector(-76, 64, 486));
	}

	public static boolean canHaveCondiments(ItemStack food) {
		if (!ItemFormat.hasDisplayName(food))
			return false;

		for (ItemStack is : AVAILABLE_FOODS) {
			if (is.getItemMeta().getDisplayName().equals(food.getItemMeta().getDisplayName()))
				return true;
		}
		return false;
	}

	/**
	 * Do not call without running canHaveCondiments() on @param food
	 */
	public static boolean applyCondiments(ItemStack food, Block dispenser) {
		boolean success = false;

		for (Vector vec : KETCHUP_LOCATIONS) {
			if (LocationUtils.isEqual(dispenser.getLocation(), vec)) {
				applyKetchup(food);
				success = true;
			}
		}

		for (Vector vec : MUSTARD_LOCATIONS) {
			if (LocationUtils.isEqual(dispenser.getLocation(), vec)) {
				applyMustard(food);
				success = true;
			}
		}

		if (success)
			dispenser.getWorld().playSound(dispenser.getLocation(), Sound.ENTITY_SLIME_JUMP, 1f, 1f);
		return success;
	}

	private static void applyKetchup(ItemStack food) {
		ItemMeta meta = food.getItemMeta();
		List<String> lore = meta.getLore();
		if (!lore.contains(ChatColor.RED + "Topped with Ketchup"))
			lore.add(lore.size() - 2, ChatColor.RED + "Topped with Ketchup");
		meta.setLore(lore);
		food.setItemMeta(meta);
	}

	private static void applyMustard(ItemStack food) {
		ItemMeta meta = food.getItemMeta();
		List<String> lore = meta.getLore();
		if (!lore.contains(ChatColor.YELLOW + "Topped with Mustard"))
			lore.add(lore.size() - 2, ChatColor.YELLOW + "Topped with Mustard");
		meta.setLore(lore);
		food.setItemMeta(meta);
	}
}
