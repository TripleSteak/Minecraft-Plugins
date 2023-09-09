package me.triplesteak.metropolis.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.triplesteak.metropolis.npc.NPCTeller;
import me.triplesteak.metropolis.world.ATM;
import me.triplesteak.metropolis.world.Elevator;

public class APCEHandler implements Listener {
	private static double chatRadius = 50; // only players within 50 blocks can see your typing

	@EventHandler
	public void onEvent(AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		Player player = event.getPlayer();
		String message = event.getMessage();

		System.out.println("<" + player.getName() + "> " + message); // broadcast to server log

		boolean broadcastToSelf = false;

		while (!message.equals("") && message.charAt(0) == ' ')
			message = message.substring(1); // remove leading spaces

		if (ATM.AWAITING_RESPONSE.containsKey(player) && ATM.AWAITING_RESPONSE.get(player)) {
			ATM.transferFunds(player, message);
			return;
		}

		if (Elevator.SELECTING_FLOOR.containsKey(player)) { // elevator floor selection
			boolean elevatorRequest = Elevator.selectFloor(player, message);
			if (elevatorRequest)
				return;
		}

		if (NPCTeller.ACTIVE_TELLERS.containsKey(player)) // teller dialogue
			NPCTeller.ACTIVE_TELLERS.get(player).playerSpeak(player, message);

		if (broadcastToSelf)
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Citizen " + ChatColor.RESET + ChatColor.WHITE
					+ player.getName() + " " + ChatColor.GRAY + message);
		else
			for (Player p : Bukkit.getOnlinePlayers()) { // modify standard chat
				if (player.getGameMode() == GameMode.CREATIVE
						|| player.getLocation().distance(p.getLocation()) <= chatRadius)
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Citizen " + ChatColor.RESET
							+ ChatColor.WHITE + player.getName() + " " + ChatColor.GRAY + message);
			}
	}
}
