package me.triplesteak.metropolis.world;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.item.Food;
import me.triplesteak.metropolis.item.JunkItem;

public final class WaterDispenser {
	public static boolean isWaterDispenser(Block block) {
		if (block.getType() == Material.SMOOTH_QUARTZ_STAIRS
				&& block.getRelative(0, 1, 0).getType() == Material.LIGHT_BLUE_STAINED_GLASS)
			return true;
		if (block.getType() == Material.LIGHT_BLUE_STAINED_GLASS
				&& block.getRelative(0, -1, 0).getType() == Material.SMOOTH_QUARTZ_STAIRS)
			return true;
		return false;
	}

	public static void giveWater(Player player) {
		for (ItemStack is : player.getInventory().getContents()) {
			if (JunkItem.isJunkItem(is)
					&& is.getItemMeta().getDisplayName().equals(JunkItem.EMPTY_PLASTIC_CUP.getItemMeta().getDisplayName())) {
				if (is.getAmount() > 1) {
					is.setAmount(is.getAmount() - 1);
					break;
				} else {
					player.getInventory().remove(is);
					break;
				}
			}
		}
		player.getInventory().addItem(Food.WATER);
	}
}
