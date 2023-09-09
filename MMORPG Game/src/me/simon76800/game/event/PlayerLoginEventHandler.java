package me.simon76800.game.event;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.mob.player.skin.SkinCustomization;
import me.simon76800.library.util.PlayerDataHandler;

public class PlayerLoginEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		PlayerDataHandler.loadFull(p, PlayerDataHandler.getCurrent(p)); // Loads player character information

		/*
		 * Change player skin
		 */
		GameProfile gp = ((CraftPlayer) p).getProfile();
		gp.getProperties().clear();
		gp.getProperties().put("textures",
				new Property("textures",
						SkinCustomization.getSkinValue(PlayerCharacter.getPlayerCharacter(p).race,
								PlayerCharacter.getPlayerCharacter(p).skinColour,
								PlayerCharacter.getPlayerCharacter(p).eyeColour),
						SkinCustomization.getSkinSignature(PlayerCharacter.getPlayerCharacter(p).race,
								PlayerCharacter.getPlayerCharacter(p).skinColour,
								PlayerCharacter.getPlayerCharacter(p).eyeColour)));
	}
}
