package me.simon76800.library.mob.player;

import java.util.Arrays;
import java.util.List;
import me.simon76800.library.map.Dimension;

public enum Races {
	/*
	 * Luminous Races
	 */
	HUMAN("human", true, 20, 20, 20, 20, 20, Arrays.asList(new Classes[] { Classes.NECROMANCER, Classes.SHAMAN, Classes.WARLOCK }),
			Dimension.ESDORFIA_MAIN, -60.0D, 76.0D, -166.0D, 0.0F),

	VILLAGER("villager", true, 16, 25, 17, 22, 20,
			Arrays.asList(new Classes[] { Classes.ASSASSIN, Classes.NECROMANCER, Classes.PALADIN, Classes.RANGER, Classes.WARLOCK}), null, 0.0D, 0.0D, 0.0D, 0.0F),

	ERFURM("erfurm", true, 23, 15, 26, 20, 16,
			Arrays.asList(new Classes[] { Classes.ASSASSIN, Classes.MAGE, Classes.NECROMANCER, Classes.RANGER }), null,
			0.0D, 0.0D, 0.0D, 0.0F),

	GRISUF("grisuf", true, 29, 19, 15, 18, 19,
			Arrays.asList(new Classes[] { Classes.ASSASSIN, Classes.KNIGHT, Classes.NECROMANCER, Classes.SHAMAN }),
			null, 0.0D, 0.0D, 0.0D, 0.0F),

	MULLUP("mullup", true, 21, 21, 16, 17, 25,
			Arrays.asList(new Classes[] { Classes.KNIGHT, Classes.NECROMANCER, Classes.PALADIN, Classes.WARLOCK }), null, 0.0D, 0.0D, 0.0D,
			0.0F),

	CACVA("cacva", true, 20, 16, 22, 25, 17, Arrays.asList(
			new Classes[] { Classes.ASSASSIN, Classes.MAGE, Classes.NECROMANCER, Classes.RANGER }), null,
			0.0D, 0.0D, 0.0D, 0.0F),

	/*
	 * Obscure Races
	 */
	UNDEAD("undead", false, 21, 18, 21, 19, 21,
			Arrays.asList(new Classes[] { Classes.ASSASSIN, Classes.PALADIN, Classes.PRIEST, Classes.RANGER }), null,
			0.0D, 0.0D, 0.0D, 0.0F),

	ODSOMO("odsomo", false, 23, 20, 15, 21, 21, Arrays.asList(new Classes[] { Classes.ASSASSIN, Classes.MAGE, Classes.PALADIN, Classes.PRIEST, Classes.WARLOCK }), null,
			0.0D, 0.0D, 0.0D, 0.0F),

	ARNAEA("arnaea", false, 16, 21, 19, 21, 23,
			Arrays.asList(new Classes[] { Classes.KNIGHT, Classes.MAGE, Classes.PRIEST, Classes.WARLOCK }), null, 0.0D, 0.0D, 0.0D,
			0.0F),

	MELIS("melis", false, 21, 21, 22, 21, 15,
			Arrays.asList(
					new Classes[] { Classes.ASSASSIN, Classes.PRIEST, Classes.RANGER, Classes.SHAMAN }),
			null, 0.0D, 0.0D, 0.0D, 0.0F),

	SNIIF("sniif", false, 22, 21, 22, 15, 20, Arrays.asList(new Classes[] { Classes.KNIGHT, Classes.PALADIN, Classes.PRIEST, Classes.SHAMAN }), null, 0.0D, 0.0D, 0.0D,
			0.0F),

	PEERREC("peerrec", false, 24, 16, 20, 20, 20,
			Arrays.asList(new Classes[] { Classes.KNIGHT, Classes.PALADIN, Classes.PRIEST, Classes.RANGER, Classes.SHAMAN, Classes.WARLOCK }), null, 0.0D, 0.0D, 0.0D,
			0.0F);

	public String name;
	public boolean isLuminous;
	public int baseStrength;
	public int baseIntellect;
	public int baseConstitution;
	public int baseStamina;
	public int baseAgility;
	public List<Classes> classBlacklist;
	public Dimension startDimension;
	public double startX;
	public double startY;
	public double startZ;
	public float startYaw;

	private Races(String name, boolean isLuminous, int baseStrength, int baseIntellect, int baseConstitution,
			int baseStamina, int baseAgility, List<Classes> classBlacklist, Dimension startDimension, double startX,
			double startY, double startZ, float startYaw) {
		this.name = name;
		this.isLuminous = isLuminous;
		this.baseStrength = baseStrength;
		this.baseIntellect = baseIntellect;
		this.baseConstitution = baseConstitution;
		this.baseStamina = baseStamina;
		this.baseAgility = baseAgility;
		this.classBlacklist = classBlacklist;

		this.startDimension = startDimension;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.startYaw = startYaw;
	}

	public String getDisplayName() {
		return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}

	public static Races getFromName(String s) {
		Races[] arrayOfRaces;
		int j = (arrayOfRaces = values()).length;
		for (int i = 0; i < j; i++) {
			Races r = arrayOfRaces[i];
			if ((r.name.equals(s)) || (r.getDisplayName().equals(s))) {
				return r;
			}
		}
		return null;
	}
}
