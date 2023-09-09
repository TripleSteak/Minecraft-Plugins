package me.triplesteak.metropolis.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class SCEHandler implements Listener {
	@EventHandler
	public void onEvent(ServerCommandEvent event) {
		if (matchingCommand(event.getCommand(), "kill")) { // "kill" command forbidden, as it may remove
															// armour stands and item frames
			event.setCancelled(true);
			event.getSender().sendMessage(ChatColor.DARK_GRAY + "This command is forbidden! Try /butcher instead.");
		}
	}

	/**
	 * 
	 * @param message command sent by command sender
	 * @param cmd     command to match, e.g. "kill"
	 */
	private boolean matchingCommand(String message, String cmd) {
		return false; //return message.toLowerCase().startsWith(cmd) || message.toLowerCase().startsWith("/" + cmd);
	}
}
