package me.simon76800.lobby.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.simon76800.library.mob.player.Classes;
import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.mob.player.Races;
import me.simon76800.library.util.PlayerDataHandler;

public class CharacterManager {
	public static final HashMap<Player, CharacterManager> CHARACTER_MANAGERS = new HashMap<Player, CharacterManager>();

	public List<PlayerCharacter> characterList = new ArrayList<PlayerCharacter>();

	private CharacterManager(Player player) {
		CHARACTER_MANAGERS.put(player, this);
	}

	/**
	 * Gets character manager of given player
	 */
	public static CharacterManager getManager(Player player) {
		if (CHARACTER_MANAGERS.containsKey(player))
			return CHARACTER_MANAGERS.get(player);
		return null;
	}

	/**
	 * Sets up character manager
	 */
	public static void setup(Player player) {
		new CharacterManager(player);
		loadCharacters(player);
	}

	/**
	 * Loads all characters
	 */
	public static void loadCharacters(Player player) {
		CharacterManager.getManager(player).characterList.clear();
		for (File f : new File(PlayerDataHandler.getCharactersDirectory(player)).listFiles()) {
			if (!f.isDirectory())
				continue;
			CharacterManager.getManager(player).characterList.add(PlayerDataHandler.loadBasic(player, f.getName()));
		}
	}

	/**
	 * Creates new character
	 */
	public static void createNewCharacter(Player player, Races race, Classes classs, int skinColour, int eyeColour) {
		PlayerDataHandler.saveBasic(player, race.startDimension.continent, race, classs, skinColour, eyeColour);
		loadCharacters(player);
	}

	/**
	 * Gets the amount of characters a player has
	 */
	public static int getCharacterCount(Player player) {
		return CHARACTER_MANAGERS.get(player).characterList.size();
	}
}
