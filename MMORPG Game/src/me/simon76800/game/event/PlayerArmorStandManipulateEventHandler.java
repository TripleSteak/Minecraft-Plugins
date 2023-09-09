package me.simon76800.game.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerArmorStandManipulateEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerArmorStandManipulateEvent e) {
		e.setCancelled(true); // prevent players from modifying armor stands
	}
}
