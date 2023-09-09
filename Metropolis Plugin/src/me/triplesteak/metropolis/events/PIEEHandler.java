package me.triplesteak.metropolis.events;

import org.bukkit.GameMode;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.triplesteak.metropolis.npc.NPC;
import me.triplesteak.metropolis.npc.NPCList;
import me.triplesteak.metropolis.npc.NPCTeller;
import me.triplesteak.metropolis.npc.NPCVendor;
import me.triplesteak.metropolis.world.ATM;
import me.triplesteak.metropolis.world.Elevator;

public class PIEEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Villager) {
			event.setCancelled(true);

			for (NPC npc : NPCList.NPC_LIST) {
				if (event.getRightClicked() == npc.getEntity()) {
					if (npc instanceof NPCVendor)
						((NPCVendor) npc).openInventory(event.getPlayer());
					else if (npc instanceof NPCTeller)
						((NPCTeller) npc).attemptOpenInventory(event.getPlayer());
				}
			}
		} else if (event.getRightClicked() instanceof ItemFrame) {
			if (Elevator.requestElevator(event.getPlayer(), event.getRightClicked().getLocation()))
				event.setCancelled(true); // elevator button clicked
			else if (ATM.isATM((ItemFrame) event.getRightClicked())) {
				ATM.openATM(event.getPlayer(), (ItemFrame) event.getRightClicked());
				event.setCancelled(true); // ATM opened
			} else if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
				event.setCancelled(true); // no rotating item frames!
		}
	}
}
