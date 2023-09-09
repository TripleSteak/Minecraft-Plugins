package me.simon76800.library.item.weapon;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

import me.simon76800.library.item.ItemQuality;
import me.simon76800.library.item.weapon.Weapon.WeaponType;
import me.simon76800.library.util.ExcelHandler;

public final class WeaponList {
	public static final HashMap<String, Weapon> WEAPONS_LIST = new HashMap<>();
	public static final HashMap<String, Integer> STRENGTH_MIN = new HashMap<>();
	public static final HashMap<String, Integer> STRENGTH_MAX = new HashMap<>();
	public static final HashMap<String, Integer> INTELLECT_MIN = new HashMap<>();
	public static final HashMap<String, Integer> INTELLECT_MAX = new HashMap<>();
	public static final HashMap<String, Integer> CONSTITUTION_MIN = new HashMap<>();
	public static final HashMap<String, Integer> CONSTITUTION_MAX = new HashMap<>();
	public static final HashMap<String, Integer> STAMINA_MIN = new HashMap<>();
	public static final HashMap<String, Integer> STAMINA_MAX = new HashMap<>();
	public static final HashMap<String, Integer> AGILITY_MIN = new HashMap<>();
	public static final HashMap<String, Integer> AGILITY_MAX = new HashMap<>();

	/**
	 * Loads weapons from database
	 */
	public static void init() {
		System.out.println("Loading in weapons...");
		
		for (WeaponType type : WeaponType.values()) {
			String[][] array = ExcelHandler.loadData("Weapons", type.getCapitalizedName());
			for (int row = 1; row < array.length; row++) {
				Weapon weapon;
				if (type.isMelee)
					weapon = new MeleeWeapon((int) Float.parseFloat(array[row][3]), array[row][2],
							ItemQuality.getFromName(array[row][0]), type, (int) Float.parseFloat(array[row][1]),
							(int) Float.parseFloat(array[row][4]), (int) Float.parseFloat(array[row][5]),
							(double) Float.parseFloat(array[row][6]), (double) Float.parseFloat(array[row][7]));
				else
					weapon = new RangedWeapon((int) Float.parseFloat(array[row][3]), array[row][2],
							ItemQuality.getFromName(array[row][0]), type, (int) Float.parseFloat(array[row][1]),
							(int) Float.parseFloat(array[row][4]), (int) Float.parseFloat(array[row][5]),
							(double) Float.parseFloat(array[row][6]), (double) Float.parseFloat(array[row][7]));

				String name = weapon.displayName;
				System.out.println(name);
				WEAPONS_LIST.put(name, weapon);
				STRENGTH_MIN.put(name, (int) Float.parseFloat(array[row][9]));
				STRENGTH_MAX.put(name, (int) Float.parseFloat(array[row][10]));
				INTELLECT_MIN.put(name, (int) Float.parseFloat(array[row][11]));
				INTELLECT_MAX.put(name, (int) Float.parseFloat(array[row][12]));
				CONSTITUTION_MIN.put(name, (int) Float.parseFloat(array[row][13]));
				CONSTITUTION_MAX.put(name, (int) Float.parseFloat(array[row][14]));
				STAMINA_MIN.put(name, (int) Float.parseFloat(array[row][15]));
				STAMINA_MAX.put(name, (int) Float.parseFloat(array[row][16]));
				AGILITY_MIN.put(name, (int) Float.parseFloat(array[row][17]));
				AGILITY_MAX.put(name, (int) Float.parseFloat(array[row][18]));
			}
			
			System.out.println("Loaded in " + (array.length - 1) + " " + type.getCapitalizedName() + "(s)");
		}
	}

	/**
	 * Not directly given to player, must be modified by
	 * {@link #getWeapon(String, Player)} or
	 * {@link #getWeapon(String, int, int, int, int, int)}
	 * 
	 * @param name
	 *            name of weapon to return
	 * @return weapon with given name, if existent
	 */
	private static Weapon getWeapon(String name) {
		if (WEAPONS_LIST.containsKey(name))
			return WEAPONS_LIST.get(name);
		return null;
	}

	/**
	 * 
	 * @return weapon with modified stats (for loading pre-existing weapons)
	 */
	public static Weapon getWeapon(String name, int strength, int intellect, int constitution, int stamina,
			int agility) {
		Weapon weapon = getWeapon(name);
		weapon.bonusStrength = strength;
		weapon.bonusIntellect = intellect;
		weapon.bonusConstitution = constitution;
		weapon.bonusStamina = stamina;
		weapon.bonusAgility = agility;
		return weapon;
	}

	/**
	 * 
	 * @return weapon of given name with randomized statistics (for new weapons)
	 */
	public static Weapon getNewWeapon(String name) {
		Weapon weapon = getWeapon(name); // TODO Modify later with loot bonus, etc.
		if (weapon == null)
			return null;

		Random rand = new Random();
		weapon.bonusStrength = rand.nextInt(STRENGTH_MAX.get(name) - STRENGTH_MIN.get(name) + 1)
				+ STRENGTH_MIN.get(name);
		weapon.bonusIntellect = rand.nextInt(INTELLECT_MAX.get(name) - INTELLECT_MIN.get(name) + 1)
				+ INTELLECT_MIN.get(name);
		weapon.bonusConstitution = rand.nextInt(CONSTITUTION_MAX.get(name) - CONSTITUTION_MIN.get(name) + 1)
				+ CONSTITUTION_MIN.get(name);
		weapon.bonusStamina = rand.nextInt(STAMINA_MAX.get(name) - STAMINA_MIN.get(name) + 1) + STAMINA_MIN.get(name);
		weapon.bonusAgility = rand.nextInt(AGILITY_MAX.get(name) - AGILITY_MIN.get(name) + 1) + AGILITY_MIN.get(name);

		return weapon;
	}
}
