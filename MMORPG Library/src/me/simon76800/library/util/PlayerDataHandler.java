package me.simon76800.library.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import me.simon76800.library.Main;
import me.simon76800.library.item.ItemQuality;
import me.simon76800.library.item.weapon.MeleeWeapon;
import me.simon76800.library.item.weapon.RangedWeapon;
import me.simon76800.library.item.weapon.Weapon;
import me.simon76800.library.item.weapon.Weapon.WeaponType;
import me.simon76800.library.item.weapon.WeaponList;
import me.simon76800.library.map.Continent;
import me.simon76800.library.map.Dimension;
import me.simon76800.library.mob.player.Classes;
import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.mob.player.Races;

public final class PlayerDataHandler {
	public static final String PLAYER_DATA_DIRECTORY = Main.PARENT_DIRECTORY + "Player Data" + File.separator;
	public static final String CURRENT_CHARACTER_FILE = "Current.txt";

	public static final String BASIC_DATA_FILE = "Basic.txt";
	public static final String EQUIPMENT_DATA_FILE = "Equipment.txt";

	public static String getPlayerDirectory(Player player) {
		File f = new File(PLAYER_DATA_DIRECTORY + player.getUniqueId().toString());
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}

	public static String getCharactersDirectory(Player player) {
		File f = new File(getPlayerDirectory(player) + File.separator + "Characters");
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}

	/**
	 * Loads full character for game
	 * 
	 * Only one PlayerCharacter can be active at any given time per player
	 */
	public static void loadFull(Player player, String characterID) {

		/*
		 * Loads basic information
		 */
		try {
			String string = FileUtils.readFileToString(new File(
					getCharactersDirectory(player) + File.separator + characterID + File.separator + BASIC_DATA_FILE),
					Main.ENCODING);
			String[] list = string.split(" ");
			new PlayerCharacter(player, characterID, Races.getFromName(list[0]), Classes.getFromName(list[1]),
					Integer.valueOf(list[2]).intValue(), Integer.valueOf(list[3]).intValue(),
					Integer.valueOf(list[4]).intValue(), Integer.valueOf(list[5]).intValue(),
					Continent.getFromName(list[6]), Dimension.getFromName(list[7]),
					Double.valueOf(list[8]).doubleValue(), Double.valueOf(list[9]).doubleValue(),
					Double.valueOf(list[10]).doubleValue(), Float.valueOf(list[11]).floatValue(),
					Float.valueOf(list[12]).floatValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * Loads equipment information
		 */
		try {
			String string = FileUtils.readFileToString(new File(getCharactersDirectory(player) + File.separator
					+ characterID + File.separator + EQUIPMENT_DATA_FILE), Main.ENCODING);
			String[] list = Pattern.compile("|", Pattern.LITERAL).split(string);

			String[] weaponList = Pattern.compile("^", Pattern.LITERAL).split(list[0]);
			if (WeaponType.getFromName(weaponList[5]).isMelee)
				PlayerCharacter.getPlayerCharacter(player).weapon = new MeleeWeapon(Short.parseShort(weaponList[2]),
						weaponList[3], ItemQuality.getFromName(weaponList[4]), WeaponType.getFromName(weaponList[5]),
						Integer.parseInt(weaponList[6]), Integer.parseInt(weaponList[7]),
						Integer.parseInt(weaponList[8]), Double.parseDouble(weaponList[9]),
						Double.parseDouble(weaponList[10]));
			else
				PlayerCharacter.getPlayerCharacter(player).weapon = new RangedWeapon(Short.parseShort(weaponList[2]),
						weaponList[3], ItemQuality.getFromName(weaponList[4]), WeaponType.getFromName(weaponList[5]),
						Integer.parseInt(weaponList[6]), Integer.parseInt(weaponList[7]),
						Integer.parseInt(weaponList[8]), Double.parseDouble(weaponList[9]),
						Double.parseDouble(weaponList[10]));
			PlayerCharacter.getPlayerCharacter(player).weapon.bonusStrength = Integer.parseInt(weaponList[11]);
			PlayerCharacter.getPlayerCharacter(player).weapon.bonusIntellect = Integer.parseInt(weaponList[12]);
			PlayerCharacter.getPlayerCharacter(player).weapon.bonusConstitution = Integer.parseInt(weaponList[13]);
			PlayerCharacter.getPlayerCharacter(player).weapon.bonusStamina = Integer.parseInt(weaponList[14]);
			PlayerCharacter.getPlayerCharacter(player).weapon.bonusAgility = Integer.parseInt(weaponList[15]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves full character
	 */
	public static void saveFull(Player player) {
		PlayerCharacter character = PlayerCharacter.CHARACTERS.get(player);

		/*
		 * Saves basic information
		 */
		try {
			File basic = new File(
					getCharactersDirectory(player) + File.separator + character + File.separator + BASIC_DATA_FILE);
			basic.createNewFile();
			FileUtils.writeStringToFile(basic,
					character.race.name + " " + character.classs.name + " " + character.skinColour + " "
							+ character.eyeColour + " " + character.level + " " + character.exp + " "
							+ character.continent.name + " " + character.dimension.name + " " + character.playerX + " "
							+ character.playerY + " " + character.playerZ + " " + character.playerYaw + " "
							+ character.playerPitch,
					Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Saves equipment information
		 */
		try {
			File equipmentFile = new File(
					getCharactersDirectory(player) + File.separator + character + File.separator + EQUIPMENT_DATA_FILE);
			equipmentFile.getParentFile().mkdirs();
			equipmentFile.createNewFile();

			Weapon currentWeapon = PlayerCharacter.getPlayerCharacter(player).weapon;
			FileUtils.writeStringToFile(equipmentFile,
					"current_weapon:^" + currentWeapon.material.name() + "^" + currentWeapon.durability + "^"
							+ currentWeapon.displayName + "^" + currentWeapon.quality.getCapitalizedName() + "^"
							+ currentWeapon.weaponType.getCapitalizedName() + "^" + currentWeapon.levelMin + "^"
							+ currentWeapon.minDamage + "^" + currentWeapon.maxDamage + "^" + currentWeapon.attackSpeed
							+ "^" + currentWeapon.attackRange + "^" + currentWeapon.bonusStrength + "^"
							+ currentWeapon.bonusIntellect + "^" + currentWeapon.bonusConstitution + "^"
							+ currentWeapon.bonusStamina + "^" + currentWeapon.bonusAgility + "|",
					Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes character
	 */
	public static void deleteCharacter(Player player, PlayerCharacter character) {
		try {
			FileUtils.deleteDirectory(
					new File(getCharactersDirectory(player) + File.separator + character.CHARACTER_ID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads basic character
	 */
	public static PlayerCharacter loadBasic(Player player, String characterID) {
		try {
			String string = FileUtils.readFileToString(new File(
					getCharactersDirectory(player) + File.separator + characterID + File.separator + BASIC_DATA_FILE),
					Main.ENCODING);
			String[] list = string.split(" ");
			return new PlayerCharacter(player, characterID, Continent.getFromName(list[6]), Races.getFromName(list[0]),
					Classes.getFromName(list[1]), Integer.valueOf(list[2]).intValue(),
					Integer.valueOf(list[3]).intValue(), Integer.valueOf(list[4]).intValue());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Saves new character
	 */
	public static void saveBasic(Player player, Continent c, Races race, Classes classs, int skinColour,
			int eyeColour) {
		String newID = PlayerCharacter.generateCharacterID();

		/*
		 * Saves basic information
		 */
		try {
			File f = new File(
					getCharactersDirectory(player) + File.separator + newID + File.separator + BASIC_DATA_FILE);
			f.getParentFile().mkdirs();
			f.createNewFile();
			FileUtils.writeStringToFile(f,
					race.name + " " + classs.name + " " + skinColour + " " + eyeColour + " 1 0 " + c.name + " "
							+ race.startDimension.name + " " + race.startX + " " + race.startY + " " + race.startZ + " "
							+ race.startYaw + " 0.0",
					Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Saves equipment information
		 */
		try {
			File equipmentFile = new File(
					getCharactersDirectory(player) + File.separator + newID + File.separator + EQUIPMENT_DATA_FILE);
			equipmentFile.getParentFile().mkdirs();
			equipmentFile.createNewFile();

			Weapon currentWeapon = WeaponList.getNewWeapon(classs.getStartWeapon().displayName);
			FileUtils.writeStringToFile(equipmentFile,
					"current_weapon:^" + currentWeapon.material.name() + "^" + currentWeapon.durability + "^"
							+ currentWeapon.displayName + "^" + currentWeapon.quality.getCapitalizedName() + "^"
							+ currentWeapon.weaponType.getCapitalizedName() + "^" + currentWeapon.levelMin + "^"
							+ currentWeapon.minDamage + "^" + currentWeapon.maxDamage + "^" + currentWeapon.attackSpeed
							+ "^" + currentWeapon.attackRange + "^" + currentWeapon.bonusStrength + "^"
							+ currentWeapon.bonusIntellect + "^" + currentWeapon.bonusConstitution + "^"
							+ currentWeapon.bonusStamina + "^" + currentWeapon.bonusAgility + "|",
					Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param player
	 * @param characterID identifier of current character being played
	 */
	public static void setCurrent(Player player, String characterID) {
		File f = new File(getCharactersDirectory(player) + File.separator + "Current.txt");
		try {
			f.createNewFile();
			FileUtils.writeStringToFile(f, characterID, Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param player
	 * @return identifier of current character being played
	 */
	public static String getCurrent(Player player) {
		try {
			return FileUtils.readFileToString(new File(getCharactersDirectory(player) + File.separator + "Current.txt"),
					Main.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
