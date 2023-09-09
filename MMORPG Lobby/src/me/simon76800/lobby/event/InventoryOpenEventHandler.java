package me.simon76800.lobby.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.simon76800.lobby.util.CreateCharacter;

public class InventoryOpenEventHandler implements Listener {
	@EventHandler
	public void onEvent(InventoryOpenEvent e) {
		CreateCharacter.updateInventory((Player) e.getPlayer());
	}
}
