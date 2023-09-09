package me.simon76800.lobby.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseEventHandler implements Listener {
	@EventHandler
	public void onEvent(InventoryCloseEvent e) {
		e.getPlayer().getInventory().clear();
	}
}
