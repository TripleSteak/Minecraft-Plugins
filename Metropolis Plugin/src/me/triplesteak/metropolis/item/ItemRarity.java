package me.triplesteak.metropolis.item;

import org.bukkit.ChatColor;

public enum ItemRarity {
	JUNK(ChatColor.GRAY, "Junk"), COMMON(ChatColor.WHITE, "Common"), UNCOMMON(ChatColor.GREEN, "Uncommon"),
	FINE(ChatColor.BLUE, "Fine"), RARE(ChatColor.AQUA, "Rare"), EPIC(ChatColor.GOLD, "Epic"),
	LEGENDARY(ChatColor.LIGHT_PURPLE, "Legendary"), MYTHICAL(ChatColor.DARK_RED, "Mythical");

	private final String prefix; // chat colour
	private final String envelope; // ending line on item lore

	ItemRarity(ChatColor prefixColour, String name) {
		this.prefix = prefixColour + "";
		this.envelope = prefixColour + name + " Item";
	}

	public String getPrefix() {
		return prefix;
	}

	public String getEnvelope() {
		return envelope;
	}
}
