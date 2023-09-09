package me.triplesteak.metropolis.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PCPEHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerCommandPreprocessEvent event) {
		if (matchingCommand(event.getMessage(), "kill")) { // "kill" command forbidden, as it may remove
															// armour stands and item frames
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "This command is forbidden!");
		}
	}

	/**
	 * 
	 * @param message command sent by command sender
	 * @param cmd     command to match, e.g. "kill"
	 */
	private boolean matchingCommand(String message, String cmd) {
		return message.toLowerCase().startsWith(cmd) || message.toLowerCase().startsWith("/" + cmd);
	}
}
