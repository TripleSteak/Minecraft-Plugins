package me.simon76800.library.item.weapon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.simon76800.library.Main;
import me.simon76800.library.item.ItemQuality;
import me.simon76800.library.mob.Mob;
import me.simon76800.library.util.CollisionHandler;

public class MeleeWeapon extends Weapon {
	public MeleeWeapon(int durability, String displayName, ItemQuality quality,
			WeaponType weaponType, int levelMin, int minDamage, int maxDamage, double attackSpeed, double attackRange) {
		super(durability, displayName, quality, weaponType, levelMin, minDamage, maxDamage, attackSpeed,
				attackRange);
	}

	@Override
	public void basicAttack(Player p) {
		for (Mob mob : Mob.MOB_LIST) {
			if (mob.getLocation().getWorld().equals(p.getWorld())) {

				Location hitLocation = CollisionHandler.hitscanCollision(p.getEyeLocation(), mob);
				if (hitLocation != null) { // player successfully hit the mob
					double distanceFromPlayer = Math.sqrt(Math.pow(hitLocation.getX() - p.getEyeLocation().getX(), 2)
							+ Math.pow(hitLocation.getY() - p.getEyeLocation().getY(), 2)
							+ Math.pow(hitLocation.getZ() - p.getEyeLocation().getZ(), 2));

					if (super.attackRange >= distanceFromPlayer) { // mob (specific point attacked) is within range
						int damageDealt = super.calculateBasicDamage(p, mob);
						mob.damage(damageDealt);

						/*
						 * Calculates position of and displays on screen damage indicator
						 */
						Location damageIndicatorLocation = new Location(hitLocation.getWorld(),
								p.getEyeLocation().getX()
										+ (1 / distanceFromPlayer) * (hitLocation.getX() - p.getEyeLocation().getX()),
								p.getEyeLocation().getY()
										+ (1 / distanceFromPlayer) * (hitLocation.getY() - p.getEyeLocation().getY())
										- 2.3,
								p.getEyeLocation().getZ()
										+ (1 / distanceFromPlayer) * (hitLocation.getZ() - p.getEyeLocation().getZ()));
						ArmorStand damageIndicatorArmorStand = Mob.getArmorStand((ArmorStand) hitLocation.getWorld()
								.spawnEntity(damageIndicatorLocation, EntityType.ARMOR_STAND));
						damageIndicatorArmorStand.setCustomName(ChatColor.RED + String.valueOf(damageDealt));

						/*
						 * Armor stand floats upward, and is destroyed after 0.7 seconds
						 */
						new BukkitRunnable() {
							int damageMovementCounter = 0;

							@Override
							public void run() {
								if(damageMovementCounter == 1) 
									damageIndicatorArmorStand.setCustomNameVisible(true);
								if (damageMovementCounter < 12) {
									damageIndicatorArmorStand
											.teleport(damageIndicatorArmorStand.getLocation().add(0, 0.04, 0));
									damageMovementCounter++;
								} else {
									damageIndicatorArmorStand.remove();
									this.cancel();
								}
							}
						}.runTaskTimer(Main.instance, 0L, 1L);
						
						/*
						 * Sets mob knockback velocities
						 */
						mob.yVelocity = 0.2F;
					}
				}
			}
		}
	}
}
