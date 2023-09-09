package me.simon76800.library.item;

import org.bukkit.Material;

public abstract class Item {
	public Material material;
	public short durability;
	public String displayName;
	
	public Item(Material material, short durability, String displayName) {
		this.material = material;
		this.durability = durability;
		this.displayName = displayName;
	}
}
