package me.triplesteak.metropolis.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import me.triplesteak.metropolis.util.BoundingRegion;

public final class BlockInteractWhitelist {
	/*
	 * Corner of whitelist bound regions
	 */
	private static final List<BoundingRegion> BOUNDS = new ArrayList<>();

	static {
		whitelistRegion(-69, 76, 459, -59, 87, 486);
	}

	private static void whitelistRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
		BOUNDS.add(new BoundingRegion(x1, y1, z1, x2, y2, z2));
	}

	public static boolean isWhitelisted(Block block) {
		for (BoundingRegion region : BOUNDS) {
			if (region.inBounds(block))
				return true;
		}
		return false;
	}
}
