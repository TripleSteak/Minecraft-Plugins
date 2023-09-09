package me.simon76800.library.mob;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import me.simon76800.library.InvisibilityToggle;

public abstract class Mob {
	public static final ArrayList<Mob> MOB_LIST = new ArrayList<>();

	public static final String HEALTH_BAR_CHAR = "\u258c";
	public static final int HEALTH_BAR_LENGTH = 16; // number of health "bars" on above head indicator

	// Mob aggression towards certain groups
	protected Hostility luminousHostility;
	protected Hostility obscureHostility;

	protected String mobName;
	protected Location location;

	// Current movement velocity in each axis direction (in blocks / tick)
	public double xVelocity = 0.0F;
	public double yVelocity = 0.0F;
	public double zVelocity = 0.0F;

	// Mob stats
	protected int mobLevel;

	protected int currentHealth;
	protected int totalHealth;

	protected boolean hasGravity = true; // if the mob is affected by gravity, flying mobs would be false

	/*
	 * Hitbox information, assuming ConstructedMob coordinate system. See
	 * #ConstructedMob.getAbsolute(double[])
	 * 
	 * hitboxX -> size 4 -> A, B, C, D hitboxY -> size 2 -> lower and upper bound
	 * (in order) hitboxZ -> size 4 -> A, B, C, D
	 */
	public double[] hitboxX;
	public double[] hitboxY;
	public double[] hitboxZ;

	/*
	 * Real hitbox coordinates (after rotation calculations) Double array with size
	 * 8, { x1, z1, x2, z2, x3, z3, x4, z4 }
	 */
	public double[] actualHitboxPoints;

	// Name tag for mobs, up to 2 (different hostility towards factions)
	protected ArmorStand[] nameTag = new ArmorStand[2];
	protected ArmorStand healthBar;
	protected final double healthBarOffset; // y-coordinate difference between health bar and mob location

	protected Mob(String mobName, Location location, Hostility luminousHostility, Hostility obscureHostility,
			int mobLevel, int totalHealth, double healthBarOffset, double[] hitboxX, double[] hitboxY,
			double[] hitboxZ) {
		this.mobName = mobName;
		this.location = location;
		this.luminousHostility = luminousHostility;
		this.obscureHostility = obscureHostility;

		this.mobLevel = mobLevel;
		this.totalHealth = totalHealth;
		this.currentHealth = totalHealth;

		this.hitboxX = hitboxX;
		this.hitboxY = hitboxY;
		this.hitboxZ = hitboxZ;
		
		this.healthBarOffset = healthBarOffset;

		nameTag[0] = getArmorStand(
				(ArmorStand) location.getWorld()
						.spawnEntity(new Location(location.getWorld(), location.getX(),
								location.getY() - 1.75 + healthBarOffset, location.getZ(), location.getYaw(),
								location.getPitch()), EntityType.ARMOR_STAND));
		nameTag[0].setCustomNameVisible(true);
		nameTag[0].setCustomName(
				luminousHostility.levelColour + "LVL " + mobLevel + " " + luminousHostility.nameColour + mobName);

		if (luminousHostility != obscureHostility) { // if mob treats all factions the same
			nameTag[1] = getArmorStand(
					(ArmorStand) location.getWorld()
							.spawnEntity(new Location(location.getWorld(), location.getX(),
									location.getY() - 1.75 + healthBarOffset, location.getZ(), location.getYaw(),
									location.getPitch()), EntityType.ARMOR_STAND));
			nameTag[1].setCustomNameVisible(true);
			nameTag[1].setCustomName(
					obscureHostility.levelColour + "LVL " + mobLevel + " " + obscureHostility.nameColour + mobName);

			InvisibilityToggle.addLuminousBlacklist(nameTag[1]);
			InvisibilityToggle.addObscureBlacklist(nameTag[0]);
		}

		healthBar = getArmorStand(
				(ArmorStand) location.getWorld().spawnEntity(new Location(location.getWorld(), location.getX(),
						location.getY() - 2 + healthBarOffset, location.getZ(), location.getYaw(), location.getPitch()),
						EntityType.ARMOR_STAND));

		MOB_LIST.add(this);

		calculateActualHitbox(); // set initial hitbox vertex locations
	}

	/**
	 * Teleport mob to new location, often needed for movement
	 * 
	 * Begin method by reinitializing mob location For constructed mobs, teleport
	 * armor stands to correct new location with getAbsolute(). Remember to always
	 * teleport the collision slime
	 * 
	 * Always call super.teleportTo() at the start of method for subclasses
	 * 
	 * @param location
	 *            new location of mob
	 */
	public void teleportTo(Location location) {
		this.location = location;

		nameTag[0].teleport(new Location(location.getWorld(), location.getX(), location.getY() - 1.75 + healthBarOffset,
				location.getZ(), location.getYaw(), location.getPitch()));
		if (luminousHostility != obscureHostility) // if mob treats all factions the same
			nameTag[1].teleport(new Location(location.getWorld(), location.getX(),
					location.getY() - 1.75 + healthBarOffset, location.getZ(), location.getYaw(), location.getPitch()));
		healthBar.teleport(new Location(location.getWorld(), location.getX(), location.getY() - 2 + healthBarOffset,
				location.getZ(), location.getYaw(), location.getPitch()));

		calculateActualHitbox();
	}

	/**
	 * Determine if the mob is currently on the ground
	 * 
	 * @return -1 if mob is not on ground, 0 if mob is on ground, and a positive
	 *         integer for the number of blocks under which the mob's feet are
	 *         submerged (e.g. stuck "in" the ground)
	 */
	public abstract int isOnGround();

	/**
	 * Teleports mob to new location relative to current location
	 * 
	 * @param xOffset
	 *            units to add along x-axis
	 * @param yOffset
	 *            units to add along y-axis
	 * @param zOffset
	 *            units to add along z-axis
	 * @param yawOffset
	 *            degrees to add to yaw
	 * @param pitchOffset
	 *            degrees to add to pitch
	 */
	public void teleportTo(double xOffset, double yOffset, double zOffset, float yawOffset, float pitchOffset) {
		teleportTo(new Location(location.getWorld(), location.getX() + xOffset, location.getY() + yOffset,
				location.getZ() + zOffset, location.getYaw() + yawOffset, location.getPitch() + pitchOffset));
	}

	/**
	 * Modifies an existing armor stand with new attributes, convenient for mob
	 * creation
	 * 
	 * @param as
	 *            original armor stand
	 * @return modified armor stand
	 */
	public static ArmorStand getArmorStand(ArmorStand as) {
		as.setInvulnerable(true);
		as.setVisible(false);
		as.setGravity(false);
		as.setBasePlate(false);
		return as;
	}

	/**
	 * Damages the mob by given amount
	 * 
	 * @param damage
	 *            exact amount damaged
	 */
	public void damage(int damage) {
		currentHealth -= damage;

		String healthBarCustomName = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RESET;
		int healthBarsRemaining = Math.round(HEALTH_BAR_LENGTH * currentHealth / totalHealth);

		for (int i = 0; i < HEALTH_BAR_LENGTH; i++) {
			if (i == HEALTH_BAR_LENGTH / 2) // write health value in the middle of the bar
				healthBarCustomName += ChatColor.DARK_RED + "" + ChatColor.BOLD + currentHealth + ChatColor.RESET;

			if (i < healthBarsRemaining)
				healthBarCustomName += ChatColor.RED + HEALTH_BAR_CHAR;
			else
				healthBarCustomName += ChatColor.DARK_GRAY + HEALTH_BAR_CHAR;
		}

		healthBarCustomName += ChatColor.DARK_RED + "" + ChatColor.BOLD + "]";
		healthBar.setCustomName(healthBarCustomName);

		if (currentHealth != totalHealth)
			healthBar.setCustomNameVisible(true);
		else
			healthBar.setCustomNameVisible(false);
	}

	/**
	 * Calculates and returns the modified coordinates of the vertices of mob's
	 * hitbox
	 * 
	 * @return double array with size 8, { x1, z1, x2, z2, x3, z3, x4, z4 }
	 */
	protected void calculateActualHitbox() {
		double[] returnArray = new double[8];

		for (int i = 0; i < 4; i++) {
			if (getLocation().getYaw() == 0.0F) {
				returnArray[i * 2] = getLocation().getX() + hitboxX[i];
				returnArray[i * 2 + 1] = getLocation().getZ() + hitboxZ[i];
				continue;
			}

			double alpha = Math.atan2(hitboxX[i], hitboxZ[i]); // in radians
			double hyp = Math.sqrt(hitboxX[i] * hitboxX[i] + hitboxZ[i] * hitboxZ[i]);

			returnArray[i * 2] = getLocation().getX() + hyp * Math.sin(alpha - getLocation().getYaw() * Math.PI / 180);
			returnArray[i * 2 + 1] = getLocation().getZ()
					+ hyp * Math.cos(alpha - getLocation().getYaw() * Math.PI / 180);
		}

		actualHitboxPoints = returnArray;
	}

	public Location getLocation() {
		return location;
	}

	public boolean hasGravity() {
		return hasGravity;
	}
}
