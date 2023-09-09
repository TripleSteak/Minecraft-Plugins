package me.simon76800.game.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.simon76800.game.GameMain;
import me.simon76800.library.mob.player.PlayerCharacter;

public final class AttackHandler {
	// Cooldown for player basic attacks
	public static final HashMap<Player, Boolean> CAN_BASIC_ATTACK = new HashMap<>();

	/**
	 * Called when player left click attacks (basic attack for most weapons),
	 * calculates hitscans and determines which mobs get damaged
	 * 
	 * @param p
	 *            player who attacks
	 */
	public static void playerLeftClickAttack(Player p) {
		if (CAN_BASIC_ATTACK.get(p)) {
			PlayerCharacter.getPlayerCharacter(p).weapon.basicAttack(p);

			CAN_BASIC_ATTACK.put(p, false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(GameMain.instance, new Runnable() {
				@Override
				public void run() {
					CAN_BASIC_ATTACK.put(p, true);
				}
			}, 10L); // TODO change to attack speed
		}
	}
}
