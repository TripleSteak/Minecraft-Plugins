package me.triplesteak.metropolis.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.triplesteak.metropolis.world.Chair;
import me.triplesteak.metropolis.world.CustomChair;
import me.triplesteak.metropolis.world.CustomChair.ChairType;

public class PIAEEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof ArmorStand) {
			event.setCancelled(true);
			ArmorStand as = (ArmorStand) event.getRightClicked();

			ChairType chairType = CustomChair.getChairType(as);
			if (chairType != null) {
				Chair.sit(event.getPlayer(), as, chairType);
				event.setCancelled(true);
			}
		}
	}
}
