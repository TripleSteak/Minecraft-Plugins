package me.triplesteak.metropolis.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.triplesteak.metropolis.item.JunkItem;

public class PDIEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerDropItemEvent event) {
		if (JunkItem.isJunkItem(event.getItemDrop().getItemStack())) { // no littering allowed!
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE
					+ "Integrity is what you do when nobody's watching... please dispose of your trash properly.");
		}
	}
}
