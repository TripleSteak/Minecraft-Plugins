package me.simon76800.library.mob.player;

import me.simon76800.library.item.weapon.Weapon;
import me.simon76800.library.item.weapon.WeaponList;

public enum Classes {
	ASSASSIN("assassin", "Blunt Dagger"), KNIGHT("knight", null), MAGE("mage", null), NECROMANCER("necromancer",
			null), PALADIN("paladin", null), PRIEST("priest",
					null), RANGER("ranger", null), SHAMAN("shaman", null), WARLOCK("warlock", null);

	public String name;
	private String startWeapon;

	private Classes(String name, String startWeapon) {
		this.name = name;
		this.startWeapon = startWeapon;
	}

	public String getDisplayName() {
		return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}

	public static Classes getFromName(String s) {
		for (Classes c : Classes.values()) {
			if ((c.name.equals(s)) || (c.getDisplayName().equals(s))) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @return start weapon for class
	 */
	public Weapon getStartWeapon() {
		return WeaponList.getNewWeapon(startWeapon);
	}
}
