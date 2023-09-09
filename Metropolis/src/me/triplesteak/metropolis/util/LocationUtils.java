package me.triplesteak.metropolis.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class LocationUtils {
	/**
	 * Determines if location has the given block coordinates
	 */
	public static final boolean isEqual(Location loc, int x, int y, int z) {
		return loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z;
	}
	
	public static final boolean isEqual(Location loc, Vector vec) {
		return isEqual(loc, vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
	}
}
