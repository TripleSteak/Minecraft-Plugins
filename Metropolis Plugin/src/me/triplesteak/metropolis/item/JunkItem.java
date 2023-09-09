package me.triplesteak.metropolis.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.item.playerhead.PlayerHead;

public final class JunkItem {
	/*
	 * List of junk items
	 */
	public static final ItemStack EMPTY_COCA_COLA_CAN = createJunk(PlayerHead.COCA_COLA.getItemStack(),
			"Empty Coca-Cola Can");
	public static final ItemStack EMPTY_PEPSI_CAN = createJunk(PlayerHead.PEPSI.getItemStack(), "Empty Pepsi Can");
	public static final ItemStack EMPTY_DIET_PEPSI_CAN = createJunk(PlayerHead.PEPSI_DIET.getItemStack(),
			"Empty Diet Pepsi Can");
	public static final ItemStack EMPTY_MONSTER_ENERGY_ULTRA_BLACK_CAN = createJunk(
			PlayerHead.MONSTER_ENERGY_ULTRA_BLACK.getItemStack(), "Empty Monster Energy Can");
	public static final ItemStack EMPTY_MONSTER_ENERGY_ULTRA_BLUE_CAN = createJunk(
			PlayerHead.MONSTER_ENERGY_ULTRA_BLUE.getItemStack(), "Empty Monster Energy Can");
	public static final ItemStack EMPTY_MONSTER_ENERGY_ULTRA_PARADISE_CAN = createJunk(
			PlayerHead.MONSTER_ENERGY_ULTRA_PARADISE.getItemStack(), "Empty Monster Energy Can");
	public static final ItemStack EMPTY_PLASTIC_BOWL = createJunk(PlayerHead.EMPTY_BOWL.getItemStack(),
			"Empty Plastic Bowl");
	public static final ItemStack EMPTY_PLASTIC_CUP = createJunk(PlayerHead.EMPTY_CUP.getItemStack(),
			"Empty Plastic Cup");
	public static final ItemStack TIN_FOIL_WRAP = createJunk(new ItemStack(Material.GOLDEN_SHOVEL), 4, "Tin Foil Wrap");
	public static final ItemStack USED_COFFEE_CUP = createJunk(PlayerHead.COFFEE_CUP.getItemStack(), "Used Coffee Cup");
	public static final ItemStack USED_HOT_DOG_TRAY = createJunk(new ItemStack(Material.GOLDEN_SHOVEL), 1,
			"Used Hot Dog Tray");
	public static final ItemStack USED_PAPER_DONUT_BAG = createJunk(new ItemStack(Material.GOLDEN_SHOVEL), 3,
			"Used Paper Donut Bag");
	public static final ItemStack USED_PAPER_SANDWICH_WRAP = createJunk(new ItemStack(Material.GOLDEN_SHOVEL), 2,
			"Used Paper Sandwich Wrap");

	public static ItemStack createJunk(ItemStack baseItem, int itemStackDamage, String name) {
		ItemMeta meta = baseItem.getItemMeta();
		meta.setDisplayName(ItemRarity.JUNK.getPrefix() + "" + ChatColor.BOLD + name);

		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "This doesn't look very useful");
		lore.add("");
		lore.add(ItemRarity.JUNK.getEnvelope());

		if (meta instanceof Damageable) {
			((Damageable) meta).setDamage(itemStackDamage);
			meta.setUnbreakable(true);
		}

		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setLore(lore);
		baseItem.setItemMeta(meta);
		return baseItem;
	}

	public static ItemStack createJunk(ItemStack baseItem, String name) {
		return createJunk(baseItem, 0, name);
	}

	public static boolean isJunkItem(ItemStack is) {
		return ItemFormat.hasLore(is) && is.getItemMeta().getLore().get(is.getItemMeta().getLore().size() - 1)
				.equals(ItemRarity.JUNK.getEnvelope());
	}
}
