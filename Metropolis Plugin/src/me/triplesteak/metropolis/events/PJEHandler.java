package me.triplesteak.metropolis.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.triplesteak.metropolis.armour.Armour;
import me.triplesteak.metropolis.savedata.BankData;

public class PJEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerJoinEvent event) { // equip player with "empty" armour if necessary
		/*
		 * Load save data
		 */
		BankData.loadBankData(event.getPlayer());
		
		/*
		 * Set equipment
		 */
		if (event.getPlayer().getEquipment().getHelmet() == null)
			event.getPlayer().getEquipment().setHelmet(Armour.emptyHelmet);
		if (event.getPlayer().getEquipment().getChestplate() == null)
			event.getPlayer().getEquipment().setChestplate(Armour.emptyChestplate);
		if (event.getPlayer().getEquipment().getLeggings() == null)
			event.getPlayer().getEquipment().setLeggings(Armour.emptyLeggings);
		if (event.getPlayer().getEquipment().getBoots() == null)
			event.getPlayer().getEquipment().setBoots(Armour.emptyBoots);
	}
}
