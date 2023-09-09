package me.triplesteak.metropolis.util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BoundingRegion {
	private int x1, y1, z1, x2, y2, z2;

	public BoundingRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;

		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;

		if (x1 > x2)
			swap(x1, x2);
		if (y1 > y2)
			swap(y1, y2);
		if (z1 > z2)
			swap(z1, z2);
	}

	public boolean inBounds(Location loc) {
		if (x1 <= loc.getX() && loc.getX() <= x2) {
			if (y1 <= loc.getY() && loc.getY() <= y2) {
				if (z1 <= loc.getZ() && loc.getZ() <= z2) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean inBounds(Block block) {
		return inBounds(block.getLocation());
	}

	public Location generateRandomWithin(World world) {
		Random rand = new Random();

		double x = rand.nextDouble() * (x2 - x1) + x1;
		double y = rand.nextDouble() * (y2 - y1) + y1;
		double z = rand.nextDouble() * (z2 - z1) + z1;

		return new Location(world, x, y, z, 0f, 0f);	
	}

	private void swap(int a, int b) {
		int temp = b;
		b = a;
		a = temp;
	}
}
