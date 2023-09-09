package me.triplesteak.metropolis.world;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.item.JunkItem;

public final class TrashCan {
	public static boolean isTrashCan(Block block) {
		if (block.getType() == Material.CAULDRON
				&& block.getRelative(0, 1, 0).getType() == Material.SMOOTH_STONE_SLAB)
			return true;
		if (block.getType() == Material.SMOOTH_STONE_SLAB
				&& block.getRelative(0, -1, 0).getType() == Material.CAULDRON)
			return true;
		return false;
	}

	public static void openTrashCan(Player player) {
		Inventory trashCan = Bukkit.createInventory(null, 27, "Trash Can");

		ItemStack throwAllJunk = new ItemStack(Material.BARRIER);
		ItemMeta meta = throwAllJunk.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Toss All Junk");
		throwAllJunk.setItemMeta(meta);
		trashCan.setItem(26, throwAllJunk);

		player.openInventory(trashCan);
	}

	public static void clearAllJunk(Player player) {
		for (ItemStack is : player.getInventory().getContents()) {
			if (JunkItem.isJunkItem(is))
				player.getInventory().remove(is);
		}
		player.closeInventory();
	}
}
