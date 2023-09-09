package me.triplesteak.metropolis.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.inventory.ItemStack;

public final class MaterialUtils {
	public static boolean isCarpet(Material material) {
		return material.name().toLowerCase().contains("carpet");
	}

	public static boolean isCarpet(Block block) {
		return isCarpet(block.getType());
	}

	public static boolean isCarpet(ItemStack is) {
		return isCarpet(is.getType());
	}

	public static boolean isWool(Material material) {
		return material.name().toLowerCase().contains("wool");
	}

	public static boolean isWool(Block block) {
		return isWool(block.getType());
	}

	public static boolean isWool(ItemStack is) {
		return isWool(is.getType());
	}

	public static boolean isSlab(Material material) {
		return material.name().toLowerCase().contains("slab");
	}

	public static boolean isSlab(Block block) {
		return isSlab(block.getType());
	}

	public static boolean isSlab(ItemStack is) {
		return isSlab(is.getType());
	}

	public static boolean isBottomSlab(Block block) {
		return isSlab(block) && ((Slab) block.getBlockData()).getType() == Type.BOTTOM;
	}
	
	public static boolean isTrapDoor(Material material) {
		return material.name().toLowerCase().contains("trapdoor");
	}
	
	public static boolean isTrapDoor(Block block) {
		return isTrapDoor(block.getType());
	}
	
	public static boolean isTrapDoor(ItemStack is) {
		return isTrapDoor(is.getType());
	}
	
	public static boolean isStrippedWood(Material material) {
		return material.name().toLowerCase().contains("stripped") && material.name().toLowerCase().contains("wood");
	}
	
	public static boolean isStrippedWood(Block block) {
		return isStrippedWood(block.getType());
	}
	
	public static boolean isStrippedWood(ItemStack is) {
		return isStrippedWood(is.getType());
	}
	
	public static boolean isStairs(Material material) {
		return material.name().toLowerCase().contains("stairs");
	}
	
	public static boolean isStairs(Block block) {
		return isStairs(block.getType());
	}
	
	public static boolean isStairs(ItemStack is) {
		return isStairs(is.getType());
	}
	
	public static boolean isSign(Material material) {
		return material.name().toLowerCase().contains("sign");
	}
	
	public static boolean isSign(Block block) {
		return isSign(block.getType());
	}
	
	public static boolean isSign(ItemStack is) {
		return isSign(is.getType());
	}
	
	public static boolean isWallSign(Material material) {
		return material.name().toLowerCase().contains("wall_sign");
	}
	
	public static boolean isWallSign(Block block) {
		return isWallSign(block.getType());
	}
	
	public static boolean isWallSign(ItemStack is) {
		return isWallSign(is.getType());
	}
}
