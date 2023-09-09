package me.simon76800.library.mob;

import org.bukkit.ChatColor;

public enum Hostility {
	PASSIVE(ChatColor.DARK_GREEN, ChatColor.GREEN), NEUTRAL(ChatColor.GOLD, ChatColor.YELLOW), HOSTILE(ChatColor.DARK_RED, ChatColor.RED);
	
	public String levelColour;
	public String nameColour;
	
	private Hostility(ChatColor levelColour, ChatColor nameColour) {
		this.levelColour = levelColour + "" + ChatColor.BOLD;
		this.nameColour = ChatColor.RESET + "" + nameColour;
	}
}
