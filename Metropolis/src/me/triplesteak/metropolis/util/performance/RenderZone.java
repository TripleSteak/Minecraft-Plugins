package me.triplesteak.metropolis.util.performance;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.util.BoundingRegion;

public enum RenderZone {
	ALL(0, 0, 0, 0, 0, 0, 64, 64, null), // special case

	TEEDEE_BANK(-104, 50, 458, -58, 170, 487, 30, 5, Metropolis.CITY_WORLD);

	private int horizRadius, vertDist;
	private BoundingRegion region;

	public World world;

	/**
	 * A render zone is a a region where entities will only spawn IF a player is in said region
	 * 
	 * Horiz radius and vert dist variables determine maximum distance from player at which entity is spawned
	 * 
	 * The first triplet of coordinates must be smaller than the second
	 */
	RenderZone(int x1, int y1, int z1, int x2, int y2, int z2, int horizRadius, int vertDist, World world) {
		this.region = new BoundingRegion(x1, y1, z1, x2, y2, z2);

		this.horizRadius = horizRadius;
		this.vertDist = vertDist;
		this.world = world;

		if (world != null)
			EntityRenderer.RENDER_ZONES_OCCUPIED.put(this, new ArrayList<>());
	}

	public boolean inRenderZone(Location loc) {
		return region.inBounds(loc);
	}

	public boolean withinDistance(Location loc1, Location loc2) {
		return (Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2) <= horizRadius
				* horizRadius && Math.abs(loc1.getY() - loc2.getY()) <= vertDist);
	}
}
