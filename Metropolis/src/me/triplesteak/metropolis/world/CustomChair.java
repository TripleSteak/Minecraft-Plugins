package me.triplesteak.metropolis.world;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.util.performance.EntityRenderer;
import me.triplesteak.metropolis.util.performance.RenderZone;

/**
 * Chairs derived from custom chair models
 */
public final class CustomChair {
	public static int idCounter = 0;
	private static final HashMap<ArmorStand, ChairType> CHAIR_ENTITIES = new HashMap<>();

	private static void createChair(Location location, ChairType chairType, RenderZone renderZone) {
		Location asLoc = new Location(Metropolis.CITY_WORLD, location.getX(), location.getY(), location.getZ(),
				location.getYaw() + chairType.yawOffset, location.getPitch());
		ArmorStand as = (ArmorStand) Metropolis.CITY_WORLD.spawnEntity(asLoc, EntityType.ARMOR_STAND);

		as.setInvulnerable(true);
		as.setGravity(false);
		as.setVisible(false);
		as.getEquipment().setHelmet(chairType.getItemStack());

		CHAIR_ENTITIES.put(as, chairType);
		
		EntityRenderer.addChair(idCounter, as, location, chairType, renderZone);
		idCounter++;
	}
	
	public static void createOldChair(Location location, ChairType chairType, int ID) {
		Location asLoc = new Location(Metropolis.CITY_WORLD, location.getX(), location.getY(), location.getZ(),
				location.getYaw() + chairType.yawOffset, location.getPitch());
		ArmorStand as = (ArmorStand) Metropolis.CITY_WORLD.spawnEntity(asLoc, EntityType.ARMOR_STAND);

		as.setInvulnerable(true);
		as.setGravity(false);
		as.setVisible(false);
		as.getEquipment().setHelmet(chairType.getItemStack());
		as.setSilent(true);

		CHAIR_ENTITIES.put(as, chairType);
		EntityRenderer.CHAIR_ID.put(ID, as.getUniqueId());
	}

	private static void createChair(double x, double y, double z, float yaw, ChairType chairType,
			RenderZone renderZone) {
		createChair(new Location(Metropolis.CITY_WORLD, x, y, z, yaw, 0.0f), chairType, renderZone);
	}

	private static void initFloors(double x, double z, float yaw, double yStart, double yIncrement, int numFloors,
			ChairType chairType, RenderZone renderZone) {
		for (int i = 0; i < numFloors; i++)
			createChair(x, yStart + i * yIncrement, z, yaw, chairType, renderZone);
	}

	public static ChairType getChairType(ArmorStand as) {
		for (ArmorStand compare : CHAIR_ENTITIES.keySet())
			if (as == compare)
				return CHAIR_ENTITIES.get(compare);
		return null;
	}

	public static void init() {
		/*
		 * TeeDee office chair
		 */
		initFloors(-92.2, 466.2, 8F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-96.1, 466.2, 0F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 470.8, 174F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-96.3, 470.9, 169F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 473.2, 8F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-96.1, 473.2, 0F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 477.8, 174F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-96.3, 477.9, 169F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-92.2, 480.2, 8F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-96.1, 480.2, 0F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-89.1, 480.2, -16F, 89.0625, 4, 16, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-72.0, 480.1, -4F, 93.0625, 4, 13, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-68.2, 480.1, 14F, 93.0625, 4, 13, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		initFloors(-64.9, 480.1, -5F, 93.0625, 4, 13, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		createChair(-92.5, 153, 480.6, -180F, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
		createChair(-67.0, 153, 468.4, 90F, ChairType.RED_GAMING_CHAIR, RenderZone.TEEDEE_BANK);
	}

	public static void destroy(ArmorStand as) {
		as.remove();
		CHAIR_ENTITIES.remove(as);
	}

	public enum ChairType {
		RED_GAMING_CHAIR(Material.DIAMOND_SHOVEL, 1, 180f, 0.625);

		private ItemStack is;

		public final float yawOffset;
		public final double sitOffset;

		/**
		 * 
		 * @param sitOffset		sitting position y-coord difference from ground
		 */
		ChairType(Material material, int damage, float yawOffset, double sitOffset) {
			ItemStack is = new ItemStack(material);
			ItemMeta meta = is.getItemMeta();

			if (meta instanceof Damageable)
				((Damageable) meta).setDamage(damage);
			is.setItemMeta(meta);
			this.is = is;

			this.yawOffset = yawOffset;
			this.sitOffset = sitOffset;
		}

		public ItemStack getItemStack() {
			return is;
		}
	}
}
