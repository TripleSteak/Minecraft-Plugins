package me.triplesteak.metropolis.events;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EDBEEHandler implements Listener {
	@EventHandler
	public void onEvent(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.ITEM_FRAME
				|| event.getEntity().getType() == EntityType.PAINTING) {
			if (event.getDamager() instanceof Player && ((Player) event.getDamager()).getGameMode() != GameMode.CREATIVE)
				event.setCancelled(true); // only creative players can remove item frames
		}
	}
}
