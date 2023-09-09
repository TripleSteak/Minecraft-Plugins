package me.simon76800.library.item;

import org.bukkit.ChatColor;

public enum ItemQuality {
	COMMON(ChatColor.WHITE, "common"), UNCOMMON(ChatColor.YELLOW, "uncommon"), RARE(ChatColor.GREEN, "rare"), EPIC(
			ChatColor.AQUA, "epic"), LEGENDARY(ChatColor.LIGHT_PURPLE, "legendary"), ULTIMATE(ChatColor.GOLD, "ultimate");

	public ChatColor displayColour;
	private String name;

	private ItemQuality(ChatColor displayColour, String name) {
		this.displayColour = displayColour;
		this.name = name;
	}
	
	public String getCapitalizedName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static ItemQuality getFromName(String s) {
		for(ItemQuality quality : ItemQuality.values()) {
			if(quality.name.equalsIgnoreCase(s)) return quality;
		}
		return null;
	}
}
