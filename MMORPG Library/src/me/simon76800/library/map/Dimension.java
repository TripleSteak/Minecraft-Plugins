package me.simon76800.library.map;

import java.util.ArrayList;

public class Dimension {
	public static final ArrayList<Dimension> DIMENSIONS = new ArrayList<Dimension>();
	public static final Dimension ESDORFIA_MAIN = new Dimension(Continent.ESDORFIA, "Esdorfia_Main");
	public String name;
	public Continent continent;

	public Dimension(Continent c, String name) {
		this.name = name;
		this.continent = c;
		c.dimensions.add(this);

		DIMENSIONS.add(this);
	}

	public static Dimension getFromName(String name) {
		for (Dimension d : DIMENSIONS) {
			if (d.name.equals(name)) {
				return d;
			}
		}
		return null;
	}
}
