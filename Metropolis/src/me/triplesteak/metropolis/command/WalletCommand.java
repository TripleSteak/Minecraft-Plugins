package me.triplesteak.metropolis.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.item.Wallet;

public final class WalletCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack is = Wallet.CREEPER_WALLET.clone();
			Wallet.setFunds(is, 100.0);
			player.getInventory().addItem(is);
			
			return true;
		}
		return false;
	}
}
