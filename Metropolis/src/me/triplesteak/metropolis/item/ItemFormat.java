package me.triplesteak.metropolis.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;

import me.triplesteak.metropolis.item.playerhead.LegacyPlayerHead;

public final class ItemFormat {
	private static final int LINE_LIMIT = 30; // minimum number of characters per line

	/**
	 * Returns a list of lore generated from the given description, split into lines
	 * Provides line gap at the end
	 */
	public static List<String> formatDescription(String description, int offset) {
		description += " ";

		String offsetStr = "";
		for (int i = 0; i < offset; i++)
			offsetStr += " ";

		List<String> list = new ArrayList<>();
		int it = 0, activeLen = 0;
		while (it + activeLen < description.length()) {
			if (activeLen < LINE_LIMIT)
				activeLen += description.substring(it + activeLen).indexOf(' ') + 1;
			else {
				list.add(offsetStr + ChatColor.GRAY + description.substring(it, it + activeLen));
				it += activeLen;
				activeLen = 0;
			}
		}
		if (!description.substring(it).equals("") && !description.substring(it).equals(" "))
			list.add(offsetStr + ChatColor.GRAY + description.substring(it));
		list.add("");

		return list;
	}

	public static List<String> formatDescription(String description) {
		return formatDescription(description, 0);
	}

	private static boolean verifyMeta(ItemStack is) {
		return is != null && is.hasItemMeta() && is.getItemMeta() != null;
	}

	public static boolean hasDisplayName(ItemStack is) {
		return verifyMeta(is) && is.getItemMeta().hasDisplayName();
	}

	public static boolean hasLore(ItemStack is) {
		return verifyMeta(is) && is.getItemMeta().hasLore();
	}
	
	/**
	 * 
	 * @return	map ID, -1 if not applicable
	 */
	@SuppressWarnings("deprecation")
	public static int getMapID(ItemStack is) {
		if(verifyMeta(is) && is.getItemMeta() instanceof MapMeta) {
			MapMeta meta = (MapMeta) is.getItemMeta();
			return meta.getMapId();
		}
		return -1;
	}

	/**
	 * 
	 * @return a list of two items, first one shown if affordable, second one shown
	 *         if not
	 */
	public static List<ItemStack> getVendorSellItem(ItemStack item, double price) {

		ItemStack[] baseItems = new ItemStack[2];
		baseItems[0] = item.clone();
		baseItems[1] = item.clone();

		for (int i = 0; i < 2; i++) {
			ItemMeta meta = baseItems[i].getItemMeta();
			List<String> lore = meta.getLore();

			lore.add("");
			if (i == 0)
				lore.add(ChatColor.DARK_GREEN + "[ $" + ChatColor.GREEN + ChatColor.BOLD + String.format("%.2f", price)
						+ ChatColor.RESET + "" + ChatColor.DARK_GREEN + " ]");
			else
				lore.add(ChatColor.DARK_RED + "[ $" + ChatColor.RED + ChatColor.BOLD + String.format("%.2f", price) + ChatColor.RESET + "" + ChatColor.DARK_RED + " ]");

			meta.setLore(lore);
			baseItems[i].setItemMeta(meta);
		}

		return Arrays.asList(baseItems[0], baseItems[1]);
	}

	/**
	 * @param item list of items that are sold in the combo (MUST be custom items)
	 * 
	 * @return a list of two items, first one shown if affordable, second one shown
	 *         if not
	 */
	public static List<ItemStack> getVendorSellCombo(List<ItemStack> item, String name, String description,
			double price) {

		ItemStack[] baseItems = new ItemStack[2];
		baseItems[0] = LegacyPlayerHead.CHEST.getItemStack().clone();
		baseItems[1] = LegacyPlayerHead.CHEST.getItemStack().clone();

		for (int i = 0; i < 2; i++) {
			ItemMeta meta = baseItems[i].getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + name);

			List<String> lore = new ArrayList<String>();

			lore.addAll(formatDescription(description));

			lore.add(ChatColor.YELLOW + "Contains:");

			for (ItemStack is : item)
				lore.add(ChatColor.GRAY + "  - " + is.getItemMeta().getDisplayName());

			lore.add("");
			if (i == 0)
				lore.add(ChatColor.DARK_GREEN + "[ $" + ChatColor.GREEN + ChatColor.BOLD + String.format("%.2f", price)
						+ ChatColor.RESET + "" + ChatColor.DARK_GREEN + " ]");
			else
				lore.add(ChatColor.DARK_RED + "[ $" + ChatColor.RED + ChatColor.BOLD + String.format("%.2f", price) + ChatColor.RESET + "" + ChatColor.DARK_RED + " ]");

			meta.setLore(lore);
			baseItems[i].setItemMeta(meta);
		}

		return Arrays.asList(baseItems[0], baseItems[1]);
	}
}
