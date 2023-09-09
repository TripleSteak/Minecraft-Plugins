package me.simon76800.game.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.simon76800.game.GameMain;
import me.simon76800.game.util.AttackHandler;
import me.simon76800.library.InvisibilityToggle;
import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.util.InventoryUtils;

public class PlayerJoinEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		Bukkit.getScheduler().scheduleSyncDelayedTask(GameMain.instance, new Runnable() {
			@Override
			public void run() {
				/*
				 * Actualize character save data
				 */
				p.teleport(PlayerCharacter.getPlayerCharacter(p).getLocation());
				p.setGameMode(GameMode.SURVIVAL);
				p.getInventory().clear();
				InventoryUtils.forceItemSlot(e.getPlayer(), 7);

				p.getInventory().setItem(7, PlayerCharacter.getPlayerCharacter(p).classs.getStartWeapon().getItemStack(p));
				
				/*
				 * Set invisibility rules for player and entities
				 */
				InvisibilityToggle.newPlayerJoined(p);
				
				AttackHandler.CAN_BASIC_ATTACK.put(p, true);
			}
		}, 1L);
	}
}
