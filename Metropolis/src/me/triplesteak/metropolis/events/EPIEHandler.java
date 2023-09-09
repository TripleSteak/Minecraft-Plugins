package me.triplesteak.metropolis.events;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EPIEHandler implements Listener {
	public static final String CANNOT_BE_PICKED = "CANNOT_BE_PICKED";

	@EventHandler
	public void onEvent(EntityPickupItemEvent event) {
		if (event.getItem().getItemStack().hasItemMeta()) {
			ItemMeta meta = event.getItem().getItemStack().getItemMeta();
			if (meta.hasLore()) {
				List<String> lore = meta.getLore();
				for (String s : lore) {
					if (s.contains(CANNOT_BE_PICKED))
						event.setCancelled(true);
				}
			}
		}
	}
}
