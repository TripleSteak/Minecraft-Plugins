package me.simon76800.lobby.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.simon76800.library.mob.player.Classes;
import me.simon76800.library.mob.player.Races;
import me.simon76800.lobby.LobbyMain;
import me.simon76800.lobby.util.CharacterManager;
import me.simon76800.lobby.util.CreateCharacter;

public class InventoryClickEventHandler implements Listener {
	@EventHandler
	public void onEvent(InventoryClickEvent e) {
		e.setCancelled(true);
		if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
				&& e.getCurrentItem().getItemMeta().hasDisplayName()) {
			Player player = (Player) e.getWhoClicked();
			String clickedItem = e.getCurrentItem().getItemMeta().getDisplayName();

			if (e.getView().getTitle().equals(CreateCharacter.INVENTORY_NAME)) {

				/*
				 * If a race was chosen
				 */
				for (Races race : Races.values()) {
					if (clickedItem.endsWith(race.getDisplayName().toUpperCase())) {
						if (!CreateCharacter.SELECTED_RACES.containsKey(player)
								|| CreateCharacter.SELECTED_RACES.get(player) != race) {
							CreateCharacter.SELECTED_RACES.put(player, race);
							if (CreateCharacter.SELECTED_CLASSES.containsKey(player))
								CreateCharacter.SELECTED_CLASSES.remove(player);
						}
						CreateCharacter.updateInventory(player);
					}
				}

				/*
				 * If a class was chosen
				 */
				for (Classes classs : Classes.values()) {
					if (clickedItem.endsWith(classs.getDisplayName().toUpperCase())) {
						if (!CreateCharacter.SELECTED_CLASSES.containsKey(player)
								|| CreateCharacter.SELECTED_CLASSES.get(player) != classs) {
							CreateCharacter.SELECTED_CLASSES.put(player, classs);
							CreateCharacter.SELECTED_SKIN.put(player, 0);
							CreateCharacter.SELECTED_EYE.put(player, 0);
						}
						CreateCharacter.updateInventory(player);
					}
				}
				/*
				 * If colour change was chosen
				 */
				if (clickedItem.equals(CreateCharacter.SKIN_COLOUR_NAME))
					CreateCharacter.nextSkin(player);
				else if (clickedItem.equals(CreateCharacter.EYE_COLOUR_NAME))
					CreateCharacter.nextEye(player);

				/*
				 * If character creation was confirmed
				 */
				else if (clickedItem.equals(CreateCharacter.CREATE_CHARACTER_NAME)) {
					CharacterManager.createNewCharacter(player, CreateCharacter.SELECTED_RACES.get(player),
							CreateCharacter.SELECTED_CLASSES.get(player), CreateCharacter.SELECTED_SKIN.get(player),
							CreateCharacter.SELECTED_EYE.get(player));
					LobbyMain.playerInteractEventHandler.refreshArmorStands(player);
					player.closeInventory();
					player.getInventory().clear();

					CreateCharacter.SELECTED_SKIN.remove(player);
					CreateCharacter.SELECTED_EYE.remove(player);
					CreateCharacter.SELECTED_CLASSES.remove(player);
					CreateCharacter.SELECTED_RACES.remove(player);
				}
			}
		}
	}
}
