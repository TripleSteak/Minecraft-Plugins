package me.simon76800.library.mob;

import org.bukkit.Location;

public abstract class ConstructedMob extends Mob {
	protected ConstructedMob(String mobName, Location location, Hostility luminousHostility, Hostility obscureHostility,
			int mobLevel, int totalHealth, double healthBarOffset, double[] hitboxX, double[] hitboxY,
			double[] hitboxZ) {
		super(mobName, location, luminousHostility, obscureHostility, mobLevel, totalHealth, healthBarOffset, hitboxX,
				hitboxY, hitboxZ);

		initItems();
		construct();
	}
	
	/**
	 * Intializes the item stacks
	 */
	protected abstract void initItems();

	/**
	 * Constructs the armor stands to prop up  the mob
	 */
	protected abstract void construct();

	/**
	 * Calculates absolute location from addition of relative offset and original
	 * location
	 * 
	 * @param vector
	 *            4-element vector to add {x, y, z, yaw}:
	 * 
	 *            The base position of the mob should always be facing the positive
	 *            Z axis, with the positive X axis 90 degrees to left. In other
	 *            words, looking down from above, if the mob faces upward, the
	 *            z-axis points upward and the x-axis points to the left. From the
	 *            same perspective, yaw increases in a clockwise direction, with
	 *            positive Z being yaw 0° and positive X being yaw -90°/270°
	 * 
	 *            In short (relative to mob facing direction)...
	 * 
	 *            ± X: left/right ± Y: up/down ± Z: forward/backward ± yaw: toward
	 *            the right, toward the left
	 * 
	 *            *Note*: avoid setting all 3 position doubles to 0 (x, y, z)
	 * 
	 * 
	 * @return new absolute location
	 */
	protected Location getAbsolute(double[] vector) {
		if (location.getYaw() == 0.0F)
			return new Location(location.getWorld(), location.getX() + vector[0], location.getY() + vector[1],
					location.getZ() + vector[2], (float) vector[3], location.getPitch());

		double alpha = Math.atan2(vector[0], vector[2]); // in radians
		double hyp = Math.sqrt(vector[0] * vector[0] + vector[2] * vector[2]);

		return new Location(location.getWorld(),
				location.getX() + hyp * Math.sin(alpha - location.getYaw() * Math.PI / 180),
				location.getY() + vector[1],
				location.getZ() + hyp * Math.cos(alpha - location.getYaw() * Math.PI / 180),
				((float) vector[3]) + location.getYaw(), location.getPitch());
	}

	@Override
	public int isOnGround() {
		int highestVal = -1; // return value, -1 for no ground contact
		for (int i = 0; i < 8; i += 2) { // loop through all four vertices of the hitbox
			Location vertexLocation = new Location(location.getWorld(), super.actualHitboxPoints[i], super.location.getY() + super.hitboxY[0] - 0.05,
					super.actualHitboxPoints[i + 1]); // location slightly underneath feet to determine if it's on ground
			int numBlocks = -1;
			
			while(vertexLocation.getBlock().getType().isSolid()) {
				numBlocks++;
				vertexLocation.add(0, 1, 0);
			}
			if(numBlocks > highestVal) highestVal = numBlocks;
		}

		return highestVal;
	}
}
