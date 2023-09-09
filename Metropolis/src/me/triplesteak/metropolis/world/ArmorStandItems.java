package me.triplesteak.metropolis.world;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.armour.Armour;
import me.triplesteak.metropolis.item.playerhead.LegacyPlayerHead;
import me.triplesteak.metropolis.item.playerhead.PlayerHead;
import me.triplesteak.metropolis.util.performance.EntityRenderer;
import me.triplesteak.metropolis.util.performance.RenderZone;

public final class ArmorStandItems {
	public static int idCounter = 0;

	public static void addNewStand(Location loc, ItemStack headItem, RenderZone renderZone) {
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.setVisible(false);
		as.getEquipment().setHelmet(headItem);

		EntityRenderer.addArmorStand(idCounter, as, loc, headItem, renderZone);
		idCounter++;
	}

	public static void addNewStand(double x, double y, double z, float yaw, float pitch, ItemStack headItem,
			RenderZone renderZone) {
		addNewStand(new Location(Metropolis.CITY_WORLD, x, y, z, yaw, pitch), headItem, renderZone);
	}

	public static void addOldStand(Location loc, ItemStack headItem, int ID) {
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.setVisible(false);
		as.setSilent(true);
		as.getEquipment().setHelmet(headItem);
		EntityRenderer.ARMORSTANDITEM_ID.put(ID, as.getUniqueId());
	}

	private static void initFloors(double x, double z, float yaw, float pitch, double yStart, double yIncrement,
			int numFloors, ItemStack headItem, RenderZone renderZone) {
		for (int i = 0; i < numFloors; i++)
			addNewStand(x, yStart + i * yIncrement, z, yaw, pitch, headItem, renderZone);
	}

	public static ItemStack getItemStack(Material material, int damage) {
		ItemStack is = new ItemStack(material);
		ItemMeta meta = is.getItemMeta();
		if (meta instanceof Damageable)
			((Damageable) meta).setDamage(damage);
		is.setItemMeta(meta);
		return is;
	}

	public static void initStands() {
		/*
		 * Hot dog and coke at Jose's Hot Dog Stand
		 */
		addNewStand(-76.5, 62.5, 489.5, 29F, 0F, PlayerHead.HOT_DOG.getItemStack(), RenderZone.ALL);
		addNewStand(-74.5, 62.6, 487.5, 11F, 0F, PlayerHead.COCA_COLA.getItemStack(), RenderZone.ALL);

		/*
		 * Donuts, trays, coffee cups, and coffee kettles at Bank's GrisTim Horton's
		 */
		addNewStand(-98.5, 62.5, 478.5, 0F, 0F, PlayerHead.DONUT_DARK_PINK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-98.5, 61.5, 478.5, 0F, 0F, PlayerHead.DONUT_WHITE.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-97.5, 62.5, 478.5, 0F, 0F, PlayerHead.DONUT_CHOCOLATE.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-97.5, 61.5, 478.5, 0F, 0F, PlayerHead.DONUT_LIGHT_PINK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-100.5, 62.5, 476.5, 12F, 0F, PlayerHead.COFFEE_CUP.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-100.5, 63.3, 476.5, 12F, 0F, PlayerHead.COFFEE_CUP.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-100.5, 64.1, 476.5, 12F, 0F, PlayerHead.COFFEE_CUP.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-100.5, 64.9, 476.5, 12F, 0F, PlayerHead.COFFEE_CUP.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-102.5, 62.5, 476.7, 5F, 0F, PlayerHead.COFFEE_MACHINE.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-98.5, 62.5, 476.7, -4F, 0F, PlayerHead.COFFEE_MACHINE.getItemStack(), RenderZone.TEEDEE_BANK);

		/*
		 * Lobby features in TeeDee bank HQ (newspaper stacks, teller's computers)
		 */
		addNewStand(-96.5, 61.5, 459.5, 180F, 0F, PlayerHead.NEWSPAPER_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-97.5, 61.5, 459.5, 180F, 0F, PlayerHead.NEWSPAPER_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-60.5, 62.5, 459.5, 180F, 0F, PlayerHead.NEWSPAPER_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-73.5, 64, 464.5, 120F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-76.5, 64, 464.5, 120F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-79.5, 64, 464.5, 120F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-82.5, 64, 464.5, 120F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-85.5, 64, 464.5, 120F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);

		/*
		 * Second floor features in TeeDee bank HQ (water dispenser, heads, and toilets)
		 */
		addNewStand(-71.25, 76.5, 462.13, 45F, 0F, Armour.getColouredLeatherHelmet(Color.RED), RenderZone.TEEDEE_BANK);
		addNewStand(-71.75, 76.5, 462.13, 45F, 0F, Armour.getColouredLeatherHelmet(Color.BLUE), RenderZone.TEEDEE_BANK);
		addNewStand(-70.13, 144.5, 477.75, 135F, 0F, Armour.getColouredLeatherHelmet(Color.RED),
				RenderZone.TEEDEE_BANK);
		addNewStand(-70.13, 144.5, 477.25, 135F, 0F, Armour.getColouredLeatherHelmet(Color.BLUE),
				RenderZone.TEEDEE_BANK);
		addNewStand(-84.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-83.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-81.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-80.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-79.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-77.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-76.5, 76.5, 462.5, 0F, 0F, LegacyPlayerHead.NOLAN.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-68.5, 481.5, 0F, 0F, 76, 4, 3, LegacyPlayerHead.WATER.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-68.5, 485.5, 0F, 0F, 76, 4, 3, LegacyPlayerHead.WATER.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-59.5, 484.5, 0F, 0F, 76, 4, 3, LegacyPlayerHead.WATER.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-60.5, 76, 460.5, 0F, 0F, LegacyPlayerHead.WATER.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-77, 78, 467.5, 33F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-91.5, 78, 461, 90F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);

		/*
		 * Third floor features in TeeDee bank HQ (cafeteria goods)
		 */
		addNewStand(-81.5, 81.5, 466.5, 0F, 0F, PlayerHead.BURGER.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-80.5, 81.5, 466.5, 0F, 0F, PlayerHead.BOWL_SALAD_TOMATO.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-79.5, 81.5, 466.5, 0F, 0F, PlayerHead.CAKE_LIGHT_PINK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-88.5, 80.5, 466.5, 0F, 0F, PlayerHead.MONSTER_ENERGY_ULTRA_BLUE.getItemStack(),
				RenderZone.TEEDEE_BANK);
		addNewStand(-87.5, 80.5, 466.5, 0F, 0F, PlayerHead.MONSTER_ENERGY_ULTRA_BLACK.getItemStack(),
				RenderZone.TEEDEE_BANK);
		addNewStand(-86.5, 80.5, 466.5, 0F, 0F, PlayerHead.MONSTER_ENERGY_ULTRA_PARADISE.getItemStack(),
				RenderZone.TEEDEE_BANK);
		addNewStand(-88.5, 81.5, 466.5, 0F, 0F, PlayerHead.MILK_GLASS.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-87.5, 81.5, 466.5, 0F, 0F, PlayerHead.ORANGE_JUICE.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-86.5, 81.5, 466.5, 0F, 0F, PlayerHead.WATER_CUP.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-88.5, 82.5, 466.5, 0F, 0F, PlayerHead.COCA_COLA.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-87.5, 82.5, 466.5, 0F, 0F, PlayerHead.PEPSI.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-86.5, 82.5, 466.5, 0F, 0F, PlayerHead.PEPSI_DIET.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-90.5, 81, 466.5, 0F, 0F, new ItemStack(Material.CHAINMAIL_HELMET), RenderZone.TEEDEE_BANK);
		addNewStand(-91.2, 81, 466.5, 0F, 0F, new ItemStack(Material.CHAINMAIL_HELMET), RenderZone.TEEDEE_BANK);
		addNewStand(-91.9, 81, 466.5, 0F, 0F, new ItemStack(Material.CHAINMAIL_HELMET), RenderZone.TEEDEE_BANK);
		addNewStand(-82.5, 81.7, 466.5, 0F, 0F, PlayerHead.EMPTY_BOWL.getItemStack(), RenderZone.TEEDEE_BANK);

		/*
		 * Office features in TeeDee Bank HQ (computers, office supplies)
		 */
		initFloors(-92.2, 467.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-96.0, 467.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 469.5, 0F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-96.0, 469.5, 0F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 474.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-96.0, 474.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 476.5, 0F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-96.0, 476.5, 0F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 481.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-96.0, 481.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-89.0, 481.5, 180F, 0F, 90, 4, 16, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-65.0, 481.5, 180F, 0F, 94, 4, 13, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-68.4, 481.5, 180F, 0F, 94, 4, 13, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-72.0, 481.5, 180F, 0F, 94, 4, 13, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		initFloors(-93.5, 466.5, 64F, 0F, 88.5, 4, 16, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-93.7, 470.6, 32F, 0F, 88.5, 4, 16, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-93.5, 473.5, 64F, 0F, 88.5, 4, 16, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-93.7, 477.6, 32F, 0F, 88.5, 4, 16, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-93.7, 480.6, 32F, 0F, 88.5, 4, 16, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		initFloors(-69.7, 479.8, 51F, 0F, 92.5, 4, 13, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);

		/*
		 * Top floor office features in TeeDee Bank HQ
		 */
		addNewStand(-92.5, 154, 479.5, 0F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-68.5, 154, 468.5, -90F, 0F, getItemStack(Material.DIAMOND_SHOVEL, 2), RenderZone.TEEDEE_BANK);
		addNewStand(-92, 152.7, 464.5, 212F, 0F, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-93, 152.7, 468, 188F, 0F, PlayerHead.NEWSPAPER_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-95.5, 153.7, 472.5, 173F, 0F, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-89.5, 152.7, 477.3, 143F, 0F, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
		addNewStand(-69.5, 152.7, 464.5, 178F, 0F, PlayerHead.PAPERS_STACK.getItemStack(), RenderZone.TEEDEE_BANK);
	}
}
