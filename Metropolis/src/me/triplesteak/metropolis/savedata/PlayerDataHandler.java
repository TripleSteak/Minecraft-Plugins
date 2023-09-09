package me.triplesteak.metropolis.savedata;

import java.io.File;

import org.bukkit.entity.Player;

public final class PlayerDataHandler {
	public static final String PARENT_DIRECTORY = "D:" + File.separator + "Games" + File.separator + "Minecraft"
			+ File.separator + "servers" + File.separator + "Metropolis" + File.separator;
	public static final String PLAYER_DATA_DIRECTORY = PARENT_DIRECTORY + "Player Data" + File.separator;
	
	public static final String ENCODING = "UTF-8";
	
	public static String getPlayerDirectory(Player player) {
		File f = new File(PLAYER_DATA_DIRECTORY + player.getUniqueId().toString());
		if (!f.exists())
			f.mkdirs();
		return f.getAbsolutePath();
	}
	
	public static String getSubdirectory(Player player, String name) {
		File f = new File(getPlayerDirectory(player) + File.separator + name);
		if(!f.exists())
			f.mkdirs();
		return f.getAbsolutePath();
	}
}
