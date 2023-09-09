package me.triplesteak.metropolis.armour;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class Armour {
	public static ItemStack emptyHelmet;
	public static ItemStack emptyChestplate;
	public static ItemStack emptyLeggings;
	public static ItemStack emptyBoots;

	static {
		emptyHelmet = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta emptyHelmetMeta = emptyHelmet.getItemMeta();
		((Damageable) emptyHelmetMeta).setDamage(1);
		emptyHelmetMeta.setDisplayName(ChatColor.GRAY + "No Helmet Equipped");
		emptyHelmetMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Right-click with helmet in hand to equip."));
		emptyHelmetMeta.setUnbreakable(true);
		emptyHelmetMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		emptyHelmetMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		emptyHelmet.setItemMeta(emptyHelmetMeta);

		emptyChestplate = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta emptyChestplateMeta = emptyChestplate.getItemMeta();
		((Damageable) emptyChestplateMeta).setDamage(1);
		emptyChestplateMeta.setDisplayName(ChatColor.GRAY + "No Chestplate Equipped");
		emptyChestplateMeta
				.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Right-click with chestplate in hand to equip."));
		emptyChestplateMeta.setUnbreakable(true);
		emptyChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		emptyChestplateMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		emptyChestplate.setItemMeta(emptyChestplateMeta);

		emptyLeggings = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta emptyLeggingsMeta = emptyLeggings.getItemMeta();
		((Damageable) emptyLeggingsMeta).setDamage(1);
		emptyLeggingsMeta.setDisplayName(ChatColor.GRAY + "No Leggings Equipped");
		emptyLeggingsMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Right-click with leggings in hand to equip."));
		emptyLeggingsMeta.setUnbreakable(true);
		emptyLeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		emptyLeggingsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		emptyLeggings.setItemMeta(emptyLeggingsMeta);

		emptyBoots = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta emptyBootsMeta = emptyBoots.getItemMeta();
		((Damageable) emptyBootsMeta).setDamage(1);
		emptyBootsMeta.setDisplayName(ChatColor.GRAY + "No Boots Equipped");
		emptyBootsMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Right-click with boots in hand to equip."));
		emptyBootsMeta.setUnbreakable(true);
		emptyBootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		emptyBootsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		emptyBoots.setItemMeta(emptyBootsMeta);
	}
	
	public static ItemStack getColouredLeatherHelmet(Color c) {
		ItemStack is = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		meta.setColor(c);
		is.setItemMeta(meta);
		return is;
	}
}
