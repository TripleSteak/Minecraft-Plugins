package me.triplesteak.metropolis.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.item.Condiments;
import me.triplesteak.metropolis.item.Food;
import me.triplesteak.metropolis.item.Wallet;
import me.triplesteak.metropolis.util.MaterialUtils;
import me.triplesteak.metropolis.world.BlockInteractWhitelist;
import me.triplesteak.metropolis.world.Chair;
import me.triplesteak.metropolis.world.TrashCan;
import me.triplesteak.metropolis.world.WaterDispenser;

public class PIEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractEvent event) {
		ItemStack mainItem = event.getPlayer().getEquipment().getItemInMainHand();
		ItemStack offItem = event.getPlayer().getEquipment().getItemInOffHand();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // wallet
																											// check
			if (Wallet.isWallet(mainItem) || Wallet.isWallet(offItem)) {
				event.setCancelled(true);

				if (event.getAction() == Action.RIGHT_CLICK_AIR) // only send message for air clicks
					event.getPlayer()
							.sendMessage(ChatColor.GREEN + "Your current wallet balance is " + ChatColor.DARK_GREEN
									+ "$" + ChatColor.DARK_GREEN
									+ String.format("%.2f", Wallet.checkFunds(mainItem) + Wallet.checkFunds(offItem))
									+ ChatColor.DARK_GREEN + ".");
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (Food.isConsumable(mainItem)) {
				event.setCancelled(true);
				Food.eatFood(event.getPlayer(), mainItem);
			} else if (Food.isConsumable(offItem)) {
				event.setCancelled(true);
				Food.eatFood(event.getPlayer(), offItem);
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clicked = event.getClickedBlock();
			if (clicked != null && clicked.getType() != Material.AIR) {
				if (Food.isConsumable(mainItem) || Food.isConsumable(offItem))
					event.setCancelled(true); // prevent placing of food

				if (clicked.getState() instanceof InventoryHolder)
					event.setCancelled(true);

				if (!BlockInteractWhitelist.isWhitelisted(clicked) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
					if (MaterialUtils.isTrapDoor(clicked))
						event.setCancelled(true);
					if (clicked.getType() == Material.LEVER)
						event.setCancelled(true);
				}

				int isChair = Chair.isChair(clicked); // check to see if player should sit on chair
				if (isChair != -1 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
					if (event.getHand() == EquipmentSlot.HAND)
						Chair.sit(event.getPlayer(), clicked, isChair);
				} else if (TrashCan.isTrashCan(clicked)) {
					event.setCancelled(true);
					if (event.getHand() == EquipmentSlot.HAND)
						TrashCan.openTrashCan(event.getPlayer());
				} else if (WaterDispenser.isWaterDispenser(clicked)) {
					event.setCancelled(true);
					if (event.getHand() == EquipmentSlot.HAND)
						WaterDispenser.giveWater(event.getPlayer());
				} else {
					// apply mustard or ketchup, if appropriate
					boolean condimentsApplied = false;
					if (Condiments.canHaveCondiments(mainItem))
						condimentsApplied = Condiments.applyCondiments(mainItem, clicked);
					if (Condiments.canHaveCondiments(offItem))
						condimentsApplied = Condiments.applyCondiments(offItem, clicked);

					if (!condimentsApplied) {
						if (Food.isConsumable(mainItem)) {
							event.setCancelled(true);
							Food.eatFood(event.getPlayer(), mainItem);
						} else if (Food.isConsumable(offItem)) {
							event.setCancelled(true);
							Food.eatFood(event.getPlayer(), offItem);
						}
					}
				}
			}
		}
	}
}
