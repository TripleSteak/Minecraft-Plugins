package me.triplesteak.metropolis.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.item.ItemFormat;
import me.triplesteak.metropolis.npc.NPCTeller;
import me.triplesteak.metropolis.npc.NPCVendor;
import me.triplesteak.metropolis.world.ATM;
import me.triplesteak.metropolis.world.TrashCan;

public class ICEHandler implements Listener {
	@EventHandler
	public void onEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();

		if (event.getClickedInventory() != null) {
			if (event.getSlotType().equals(SlotType.ARMOR)) { // prevent equipment of armour through inventory
				event.setCancelled(true);
				player.closeInventory();
			}
			
			if (NPCVendor.LAST_VENDOR.containsKey(player)
					&& NPCVendor.LAST_VENDOR.get(player).getInventoryName().equals(event.getView().getTitle())) {
				// clicked into NPC vendor inventory
				event.setCancelled(true);
				player.updateInventory();

				if (event.getClickedInventory().getType() != InventoryType.PLAYER) // if player clicked NPC inventory
					NPCVendor.LAST_VENDOR.get(player).clickSlot((Player) player, slot);
			}
			
			if (NPCTeller.ACTIVE_TELLERS.containsKey(player)) {
				if (event.getView().getTitle().contains("Active")) {
					event.setCancelled(true);
					player.updateInventory();
				} else if (NPCTeller.makeInventoryChoice(player, slot, event.getView().getTitle())) {
					event.setCancelled(true);
					player.updateInventory();
				}
			} 
			
			if (ATM.ACTIVE_BANKS.containsKey(player) && event.getView().getTitle()
					.startsWith(ChatColor.DARK_GREEN + ATM.ACTIVE_BANKS.get(player).name)) { // ATM selections
				if (event.getView().getTitle().contains("ATM")) {
					event.setCancelled(true);
					player.updateInventory();

					if (slot == 2)
						ATM.setActiveOption(player, 1);
					else if (slot == 4)
						ATM.setActiveOption(player, 2);
					else if (slot == 6)
						ATM.setActiveOption(player, 3);
				} else if (event.getView().getTitle().contains("Account")) {
					event.setCancelled(true);
					player.updateInventory();
					
					ItemStack is = event.getCurrentItem();
					if (ItemFormat.hasDisplayName(is))
						ATM.selectAccount(player, is.getItemMeta().getDisplayName());
				}
			}
			
			if (event.getView().getTitle().startsWith("Trash Can")) { // trash can interface
				if (slot == 26
						|| (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER)) {
					TrashCan.clearAllJunk((Player) player);
					event.setCancelled(true);
					player.updateInventory();
				}
				return;
			} 
		}
	}
}
