package me.triplesteak.metropolis.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class BlockUtils {
	/**
	 * 
	 * @return if the given @param block is horizontally adjacent to wool
	 */
	public static boolean adjacentToWool(Block block, int quantity) {
		int counter = 0;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (MaterialUtils.isWool(block.getWorld().getBlockAt(block.getLocation().clone().add(x, 0, z))))
					counter++;
			}
		}
		return counter >= quantity;
	}

	public static boolean adjacentToTrapdoor(Block block, int quantity) {
		int counter = 0;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (MaterialUtils.isTrapDoor(block.getWorld().getBlockAt(block.getLocation().clone().add(x, 0, z))))
					counter++;
			}
		}
		return counter >= quantity;
	}
	
	public static BlockFace getCWFace(BlockFace facing) {
		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(int i = 0; i < 4; i++) if(facing == faces[i]) return faces[(i + 1) % 4];
		return null;
	}
	
	public static BlockFace getCCWFace(BlockFace facing) {
		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(int i = 0; i < 4; i++) if(facing == faces[i]) return faces[(i + 3) % 4];
		return null;
	}
}
