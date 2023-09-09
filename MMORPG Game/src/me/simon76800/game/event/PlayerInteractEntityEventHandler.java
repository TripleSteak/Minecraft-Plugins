package me.simon76800.game.event;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) e.setCancelled(true);
	}
}
