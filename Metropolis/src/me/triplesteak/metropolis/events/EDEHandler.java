package me.triplesteak.metropolis.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.world.Chair;

public class EDEHandler implements Listener {
	@EventHandler
	public void onEvent(EntityDismountEvent event) {
		if (event.getEntity() instanceof Player) {
			boolean chairDismount = Chair.dismount((Player) event.getEntity());
			if(chairDismount) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Metropolis.instance, new Runnable() {
					@Override
					public void run() {
						event.getEntity().teleport(event.getEntity().getLocation().add(0, 1, 0));
					}
				}, 2L);
			}
		}
	}
}
