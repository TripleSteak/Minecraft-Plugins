package me.simon76800.library.mob.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.simon76800.library.item.weapon.Weapon;
import me.simon76800.library.map.Continent;
import me.simon76800.library.map.Dimension;

public class PlayerCharacter {
	public static final HashMap<Player, PlayerCharacter> CHARACTERS = new HashMap<Player, PlayerCharacter>();
	
	// Basic Data
	public String CHARACTER_ID;
	public Player player;
	public Races race;
	public Classes classs;
	public int skinColour;
	public int eyeColour;
	
	public int level;
	public int exp;
	
	public Continent continent;
	public Dimension dimension;
	public double playerX;
	public double playerY;
	public double playerZ;
	public float playerYaw;
	public float playerPitch;
	
	// Inventory Data
	public Weapon weapon;

	/**
	 * Creates basic player character for the game
	 * 
	 */
	public PlayerCharacter(Player player, String characterID, Races race, Classes classs, int skinColour, int eyeColour,
			int level, int exp, Continent continent, Dimension dimension, double playerX, double playerY,
			double playerZ, float playerYaw, float playerPitch) {
		this(player, characterID, continent, race, classs, skinColour, eyeColour, level);
		this.exp = exp;
		this.dimension = dimension;
		this.playerX = playerX;
		this.playerY = playerY;
		this.playerZ = playerZ;
		this.playerYaw = playerYaw;
		this.playerPitch = playerPitch;
		if (CHARACTERS.containsKey(player)) {
			CHARACTERS.remove(player);
		}
		CHARACTERS.put(player, this);
	}

	/**
	 * Creates basic player character for lobby
	 * 
	 */
	public PlayerCharacter(Player player, String characterID, Continent c, Races race, Classes classs, int skinColour,
			int eyeColour, int level) {
		this.CHARACTER_ID = characterID;
		this.player = player;
		this.race = race;
		this.classs = classs;
		this.skinColour = skinColour;
		this.eyeColour = eyeColour;

		this.level = level;

		this.continent = c;
	}

	/**
	 * Creates new basic player character
	 * 
	 */
	public PlayerCharacter(Player player, Continent c, Races race, Classes classs, int skinColour, int eyeColour,
			int level) {
		this(player, generateCharacterID(), c, race, classs, skinColour, eyeColour, level);
	}

	/**
	 * 
	 * @return unique identifier for player character
	 */
	public static String generateCharacterID() {
		return System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
	}

	public Location getLocation() {
		return new Location(Bukkit.getWorld(this.dimension.name), this.playerX, this.playerY, this.playerZ,
				this.playerYaw, this.playerPitch);
	}
	
	/**
	 * Returns current player character
	 * 
	 * @param p player who owns character
	 * @return PlayerCharacter instance
	 */
	public static PlayerCharacter getPlayerCharacter(Player p) {
		return CHARACTERS.get(p);
	}
}
