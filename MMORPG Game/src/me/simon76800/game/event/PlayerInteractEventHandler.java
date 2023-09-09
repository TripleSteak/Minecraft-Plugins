package me.simon76800.game.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.simon76800.game.util.AttackHandler;

public class PlayerInteractEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			AttackHandler.playerLeftClickAttack(e.getPlayer());
		}
	}
}
