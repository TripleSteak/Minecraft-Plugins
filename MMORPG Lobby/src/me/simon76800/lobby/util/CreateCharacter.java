package me.simon76800.lobby.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.simon76800.library.mob.player.Classes;
import me.simon76800.library.mob.player.Races;
import me.simon76800.library.mob.player.skin.SkinCustomization;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public final class CreateCharacter {
	public static final HashMap<Player, Inventory> INVENTORIES = new HashMap<Player, Inventory>();
	public static final HashMap<Player, Races> SELECTED_RACES = new HashMap<Player, Races>();
	public static final HashMap<Player, Classes> SELECTED_CLASSES = new HashMap<Player, Classes>();
	public static final HashMap<Player, Integer> SELECTED_SKIN = new HashMap<Player, Integer>();
	public static final HashMap<Player, Integer> SELECTED_EYE = new HashMap<Player, Integer>();

	public static final HashMap<Races, List<String>> RACE_DESCRIPTIONS = new HashMap<Races, List<String>>();
	public static final HashMap<Classes, List<String>> CLASS_DESCRIPTIONS = new HashMap<Classes, List<String>>();

	public static final String INVENTORY_NAME = " ";

	private static final String LUMINOUS_COLOUR = "" + ChatColor.AQUA + ChatColor.BOLD;
	private static final String OBSCURE_COLOUR = "" + ChatColor.RED + ChatColor.BOLD;
	private static final String FACTION_COLOUR = "" + ChatColor.GRAY;
	private static final String CLASS_COLOUR = "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD;

	public static final String SKIN_COLOUR_NAME = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Skin Colour";
	public static final String EYE_COLOUR_NAME = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Eye Colour";
	public static final String CREATE_CHARACTER_NAME = "" + ChatColor.GREEN + ChatColor.BOLD + "Create Character!";

	private static final String SELECTED = "" + ChatColor.GREEN + ChatColor.BOLD + "SELECTED";

	/**
	 * Initializes race and class descriptions
	 */
	public static void init() {
		RACE_DESCRIPTIONS.put(Races.HUMAN, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.VILLAGER, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.ERFURM, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.GRISUF, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.MULLUP, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.CACVA, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.UNDEAD, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.ODSOMO, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.ARNAEA, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.MELIS, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.SNIIF, Arrays.asList("fit3 m3 n00b"));
		RACE_DESCRIPTIONS.put(Races.PEERREC, Arrays.asList("fit3 m3 n00b"));

		CLASS_DESCRIPTIONS.put(Classes.ASSASSIN, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.KNIGHT, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.MAGE, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.NECROMANCER, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.PALADIN, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.PRIEST, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.RANGER, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.SHAMAN, Arrays.asList("wofeij"));
		CLASS_DESCRIPTIONS.put(Classes.WARLOCK, Arrays.asList("wofeij"));
	} 

	/**
	 * Updates player inventory menu
	 */
	public static void updateInventory(Player player) {
		if (!SELECTED_SKIN.containsKey(player))
			SELECTED_SKIN.put(player, 0);
		if (!SELECTED_EYE.containsKey(player))
			SELECTED_EYE.put(player, 0);

		Inventory inv = INVENTORIES.containsKey(player) ? INVENTORIES.get(player)
				: Bukkit.createInventory(null, 36, INVENTORY_NAME);
		inv.clear();
		player.getInventory().clear();

		// Creates list of races
		for (Races race : Races.values())
			inv.setItem(Arrays.asList(Races.values()).indexOf(race) + (race.isLuminous ? 3 : 6),
					getRace(player, race, RACE_DESCRIPTIONS.get(race)));

		// Creates list of classes
		if (SELECTED_RACES.containsKey(player)) {
			for (Classes classs : Classes.values()) {
				if (!SELECTED_RACES.get(player).classBlacklist.contains(classs))
					inv.setItem(Arrays.asList(Classes.values()).indexOf(classs) + 27,
							getCharacterClass(player, classs, CLASS_DESCRIPTIONS.get(classs)));
			}
		}

		// Creates customization toggles and creation confirmation
		if (SELECTED_RACES.containsKey(player) && SELECTED_CLASSES.containsKey(player)) {
			player.getInventory().setItem(19, getSkin(player, SELECTED_RACES.get(player)));
			player.getInventory().setItem(21, getEye(player, SELECTED_RACES.get(player)));
			player.getInventory().setItem(25, getCreateCharacter(player));
		}

		INVENTORIES.put(player, inv);
	}

	/**
	 * Selects next skin colour
	 */
	public static void nextSkin(Player player) {
		SELECTED_SKIN.put(player,
				SELECTED_SKIN.get(player)
						.intValue() >= SkinCustomization.SKIN_COLOURS.get(SELECTED_RACES.get(player)).size() - 1 ? 0
								: SELECTED_SKIN.get(player).intValue() + 1);
		updateInventory(player);
	}

	/**
	 * Selects next eye colour
	 */
	public static void nextEye(Player player) {
		SELECTED_EYE.put(player,
				SELECTED_EYE.get(player)
						.intValue() >= SkinCustomization.EYE_COLOURS.get(SELECTED_RACES.get(player)).size() - 1 ? 0
								: SELECTED_EYE.get(player).intValue() + 1);
		updateInventory(player);
	}

	/**
	 * Returns menu item for specified race
	 */
	private static ItemStack getRace(Player player, Races race, List<String> description) {
		ItemStack is = new ItemStack(Material.WOODEN_HOE, 1);
		
		ItemMeta meta = is.getItemMeta();
		((Damageable) meta).setDamage((short) (Arrays.asList(Races.values()).indexOf(race) + 6));
		meta.setDisplayName(
				FACTION_COLOUR + (race.isLuminous ? "Luminous " + LUMINOUS_COLOUR : "Obscure " + OBSCURE_COLOUR)
						+ race.getDisplayName().toUpperCase());
		meta.setUnbreakable(true);

		ArrayList<String> loreList = new ArrayList<String>(Arrays.asList("",
				getBaseStats(race.baseStrength, "Strength"), getBaseStats(race.baseIntellect, "Intellect"),
				getBaseStats(race.baseConstitution, "Constitution"), getBaseStats(race.baseStamina, "Stamina"),
				getBaseStats(race.baseAgility, "Agility"), ""));
		for (String s : description)
			loreList.add(ChatColor.WHITE + s);
		if (SELECTED_RACES.containsKey(player) && SELECTED_RACES.get(player) == race) {
			loreList.add("");
			loreList.add(SELECTED);
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		}

		meta.setLore(loreList);
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);

		return is;
	}

	/**
	 * Returns menu item for specified class
	 */
	private static ItemStack getCharacterClass(Player player, Classes classs, List<String> description) {
		ItemStack is = new ItemStack(Material.WOODEN_HOE, 1);

		ItemMeta meta = is.getItemMeta();
		((Damageable) meta).setDamage(
				(short) (Arrays.asList(Classes.values()).indexOf(classs) + 21));
		meta.setDisplayName(CLASS_COLOUR + classs.getDisplayName().toUpperCase());
		meta.setUnbreakable(true);

		ArrayList<String> loreList = new ArrayList<String>();
		for (String s : description)
			loreList.add(ChatColor.WHITE + s);
		if (SELECTED_CLASSES.containsKey(player) && SELECTED_CLASSES.get(player) == classs) {
			loreList.add("");
			loreList.add(SELECTED);
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		}

		meta.setLore(loreList);
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);

		return is;
	}

	/**
	 * Returns menu item for specified skin colour
	 */
	private static ItemStack getSkin(Player player, Races race) {
		ItemStack is = new ItemStack(Material.LEATHER_LEGGINGS, 1);

		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		((Damageable) meta).setDamage(1);
		meta.setDisplayName(SKIN_COLOUR_NAME);
		meta.setUnbreakable(true);
		meta.setLore(Arrays.asList(ChatColor.WHITE + "Click to change"));

		Color colour = SkinCustomization.SKIN_COLOURS.get(race).get(SELECTED_SKIN.get(player).intValue());
		meta.setColor(org.bukkit.Color.fromRGB(colour.getRed(), colour.getGreen(), colour.getBlue()));
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);

		return is;
	}

	/**
	 * Returns menu item for specified skin colour
	 */
	private static ItemStack getEye(Player player, Races race) {
		ItemStack is = new ItemStack(Material.LEATHER_LEGGINGS, 1);

		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		((Damageable) meta).setDamage(2);
		meta.setDisplayName(EYE_COLOUR_NAME);
		meta.setUnbreakable(true);
		meta.setLore(Arrays.asList(ChatColor.WHITE + "Click to change"));

		Color colour = SkinCustomization.EYE_COLOURS.get(race).get(SELECTED_EYE.get(player).intValue());
		meta.setColor(org.bukkit.Color.fromRGB(colour.getRed(), colour.getGreen(), colour.getBlue()));
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);

		return is;
	}

	private static ItemStack getCreateCharacter(Player player) {
		ItemStack is = new ItemStack(Material.GOLDEN_HOE, 1);

		ItemMeta meta = is.getItemMeta();
		((Damageable) meta).setDamage(1);
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Create Character!");
		meta.setUnbreakable(true);
		meta.setLore(Arrays.asList(ChatColor.WHITE + SELECTED_RACES.get(player).getDisplayName() + " "
				+ SELECTED_CLASSES.get(player).getDisplayName()));
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);

		return is;
	}

	private static String getBaseStats(int number, String stat) {
		return "" + ChatColor.YELLOW + number + " " + ChatColor.GOLD + stat;
	}
}
