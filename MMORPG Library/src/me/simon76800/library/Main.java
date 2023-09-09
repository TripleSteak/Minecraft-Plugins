package me.simon76800.library;

import org.bukkit.plugin.java.JavaPlugin;

import me.simon76800.library.item.weapon.WeaponList;
import me.simon76800.library.mob.player.skin.SkinCustomization;

public class Main extends JavaPlugin {
	public static Main instance;
	public static final String PARENT_DIRECTORY = "D:/Games/Minecraft/servers/MMORPG/";
	public static final String ENCODING = "UTF-8";

	public void onEnable() {
		instance = this;
		
		SkinCustomization.init();
		
		WeaponList.init();
		
		InvisibilityToggle.init();
	}

	public void onDisable() {
		
	}
}
