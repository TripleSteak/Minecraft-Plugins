package me.simon76800.library.map;

import java.util.ArrayList;

public class Continent {
	public static final ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	public static final Continent ESDORFIA = new Continent("ESDORFIA");
	public ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
	public String name;

	public Continent(String name) {
		this.name = name;

		CONTINENTS.add(this);
	}

	public static Continent getFromName(String name) {
		for (Continent c : CONTINENTS) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}
}
