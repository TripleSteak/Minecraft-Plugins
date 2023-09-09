package me.simon76800.game.util;

import org.bukkit.Bukkit;

import me.simon76800.game.GameMain;
import me.simon76800.library.mob.Mob;

public final class MobUpdater {
	/**
	 * Creates a Bukkit repeating task that is called once every tick
	 * 
	 * Iterates through all mobs on server and updates each mob accordingly
	 */
	public static void updateLoop() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(GameMain.instance, new Runnable() {
			@Override
			public void run() {

				/*
				 * Updates movement of all mobs
				 */
				for (Mob mob : Mob.MOB_LIST) {
					// accelerate mob downward velocity if not on ground, capped at -2.5 terminal
					if (mob.hasGravity() && mob.isOnGround() < 0 && mob.yVelocity > -2.5) {
						mob.yVelocity -= 0.1;
					}

					if (mob.isOnGround() != -1 && mob.yVelocity < 0) { // if mob just landed on ground
						mob.xVelocity = 0;
						mob.yVelocity = 0;
						mob.zVelocity = 0;

						// TODO prevent bison from falling too far into ground after falling at high
						// speeds
						mob.teleportTo(mob.xVelocity,
								Math.ceil(mob.getLocation().getY() - 0.1) - mob.getLocation().getY() + mob.isOnGround(), mob.zVelocity,
								0.0F, 0.0F); // prevent mob from being underneath surface
					} else if (mob.xVelocity != 0 || mob.yVelocity != 0 || mob.zVelocity != 0) { // if movement took
																									// place,
																									// otherwise no
						// teleport is necessary
						mob.teleportTo(mob.xVelocity, mob.yVelocity, mob.zVelocity, 0.0F, 0.0F);
					}
				}
			}
		}, 20L, 1L); // initial runthrough after 1 second, following iterations at 1 tick intervals
	}
}
