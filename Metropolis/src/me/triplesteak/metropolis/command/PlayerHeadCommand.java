package me.triplesteak.metropolis.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.triplesteak.metropolis.item.playerhead.LegacyPlayerHead;
import me.triplesteak.metropolis.item.playerhead.PlayerHead;

public class PlayerHeadCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0)
				player.sendMessage(ChatColor.DARK_GRAY + "Not enough arguments!");
			else {
				String headName = args[0];

				for (PlayerHead head : PlayerHead.values()) {
					if (headName.equalsIgnoreCase(head.toString())) {
						player.getInventory().addItem(head.getItemStack());

						return true;
					}
				}

				for (LegacyPlayerHead head : LegacyPlayerHead.values()) {
					if (headName.equalsIgnoreCase(head.toString())) {
						player.getInventory().addItem(head.getItemStack());

						return true;
					}
				}

				player.sendMessage("No such player head exists in the database!");
				return true;
			}
		}

		return false;
	}
}
