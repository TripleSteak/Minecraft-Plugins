package me.simon76800.game.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.simon76800.library.mob.MobWoodBison;

public class SpawnMobCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			@SuppressWarnings("unused")
			MobWoodBison woodBison = new MobWoodBison(((Player) sender).getLocation());
			return true;
		}
		return false;
	}
}
