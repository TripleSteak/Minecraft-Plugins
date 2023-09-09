package me.triplesteak.metropolis.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.util.performance.EntityRenderer;
import me.triplesteak.metropolis.util.performance.RenderZone;

@SuppressWarnings("deprecation")
public final class ItemFrames {
	public static final int numMaps = 41; // number of filled maps

	public static final int elevatorButtonID = 8; // map# of elevator button

	public static final ItemStack[] FILLED_MAPS = new ItemStack[numMaps];

	public static int idCounter = 0;

	static {
		for (int i = 0; i < numMaps; i++) {
			FILLED_MAPS[i] = new ItemStack(Material.FILLED_MAP);
			MapMeta map = (MapMeta) FILLED_MAPS[i].getItemMeta();
			map.setMapId(i);
			FILLED_MAPS[i].setItemMeta(map);
		}
	}

	public static void initItemFrames() {
		/*
		 * Jose's hot dog stand
		 */
		initFrame(-78, 63, 488, BlockFace.EAST, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), RenderZone.ALL);

		/*
		 * TeeDee Bank HQ GrisTim Horton's signs and menus
		 */
		initMapFrame(-103, 67, 488, BlockFace.NORTH, 0, RenderZone.ALL);
		initMapFrame(-102, 67, 488, BlockFace.NORTH, 1, RenderZone.ALL);
		initMapFrame(-101, 67, 488, BlockFace.NORTH, 2, RenderZone.ALL);
		initMapFrame(-100, 67, 488, BlockFace.NORTH, 3, RenderZone.ALL);
		initMapFrames(-99, 67, 488, -92, 67, 488, BlockFace.NORTH, 4, RenderZone.ALL);
		initMapFrame(-89, 67, 477, BlockFace.SOUTH, 4, RenderZone.TEEDEE_BANK);
		initMapFrame(-90, 67, 477, BlockFace.SOUTH, 0, RenderZone.TEEDEE_BANK);
		initMapFrame(-91, 67, 477, BlockFace.SOUTH, 1, RenderZone.TEEDEE_BANK);
		initMapFrame(-92, 67, 477, BlockFace.SOUTH, 2, RenderZone.TEEDEE_BANK);
		initMapFrame(-93, 67, 477, BlockFace.SOUTH, 3, RenderZone.TEEDEE_BANK);
		initMapFrames(-94, 67, 477, -95, 67, 477, BlockFace.SOUTH, 4, RenderZone.TEEDEE_BANK);
		initMapFrame(-102, 67, 479, BlockFace.NORTH, 5, RenderZone.TEEDEE_BANK);
		initMapFrame(-100, 67, 479, BlockFace.NORTH, 6, RenderZone.TEEDEE_BANK);
		initMapFrame(-98, 67, 479, BlockFace.NORTH, 7, RenderZone.TEEDEE_BANK);

		/*
		 * TeeDee Bank HQ third floor grill
		 */
		initFrame(-86, 83, 461, BlockFace.DOWN, new ItemStack(Material.COOKED_BEEF), RenderZone.TEEDEE_BANK);
		initFrame(-85, 83, 461, BlockFace.DOWN, new ItemStack(Material.COOKED_BEEF), RenderZone.TEEDEE_BANK);
		initFrame(-84, 83, 461, BlockFace.DOWN, new ItemStack(Material.COOKED_BEEF), RenderZone.TEEDEE_BANK);

		/*
		 * TeeDee Bank washroom signs
		 */
		initMapFrame(-62, 78, 478, BlockFace.SOUTH, 9, RenderZone.TEEDEE_BANK);
		initMapFrame(-62, 78, 476, BlockFace.NORTH, 10, RenderZone.TEEDEE_BANK);
		initMapFrame(-71, 82, 474, BlockFace.EAST, 9, RenderZone.TEEDEE_BANK);
		initMapFrame(-72, 82, 462, BlockFace.NORTH, 10, RenderZone.TEEDEE_BANK);

		/*
		 * TeeDee Bank logos
		 */
		initMapFrame(-88, 65, 485, BlockFace.WEST, 11, RenderZone.ALL);
		initMapFrame(-88, 64, 485, BlockFace.WEST, 12, RenderZone.ALL);
		initMapFrame(-88, 65, 484, BlockFace.WEST, 13, RenderZone.ALL);
		initMapFrame(-88, 64, 484, BlockFace.WEST, 14, RenderZone.ALL);

		initMapFrame(-73, 57, 464, BlockFace.EAST, 11, RenderZone.TEEDEE_BANK);
		initMapFrame(-73, 56, 464, BlockFace.EAST, 12, RenderZone.TEEDEE_BANK);
		initMapFrame(-73, 57, 465, BlockFace.EAST, 13, RenderZone.TEEDEE_BANK);
		initMapFrame(-73, 56, 465, BlockFace.EAST, 14, RenderZone.TEEDEE_BANK);

		initMapFrame(-71, 57, 465, BlockFace.WEST, 11, RenderZone.TEEDEE_BANK);
		initMapFrame(-71, 56, 465, BlockFace.WEST, 12, RenderZone.TEEDEE_BANK);
		initMapFrame(-71, 57, 464, BlockFace.WEST, 13, RenderZone.TEEDEE_BANK);
		initMapFrame(-71, 56, 464, BlockFace.WEST, 14, RenderZone.TEEDEE_BANK);

		/*
		 * TeeDee ATMs
		 */
		initMapFrame(-99, 64, 473, BlockFace.SOUTH, 16, RenderZone.TEEDEE_BANK);
		initMapFrame(-101, 64, 473, BlockFace.SOUTH, 16, RenderZone.TEEDEE_BANK);
		initMapFrame(-102, 64, 472, BlockFace.WEST, 16, RenderZone.TEEDEE_BANK);
		initMapFrame(-102, 64, 470, BlockFace.WEST, 16, RenderZone.TEEDEE_BANK);

		/*
		 * TeeDee filing cabinets
		 */
		initFrame(-67, 153, 466, BlockFace.NORTH, new ItemStack(Material.STONE_SLAB), RenderZone.TEEDEE_BANK);
		initFrame(-66, 153, 466, BlockFace.NORTH, new ItemStack(Material.STONE_SLAB), RenderZone.TEEDEE_BANK);
		initFrame(-67, 154, 466, BlockFace.NORTH, new ItemStack(Material.STONE_SLAB), RenderZone.TEEDEE_BANK);
		initFrame(-66, 154, 467, BlockFace.NORTH, new ItemStack(Material.STONE_SLAB), RenderZone.TEEDEE_BANK);

		/*
		 * The Streusel Times logo
		 */
		for (int i = 0; i < 24; i++) {
			initMapFrame(-108, 75 - (i % 2), 424 + (i / 2), BlockFace.EAST, 17 + i, RenderZone.ALL);
			initMapFrame(-54, 75 - (i % 2), 435 - (i / 2), BlockFace.WEST, 17 + i, RenderZone.ALL);
		}

		/*
		 * Stop signs
		 */
		initMapFrame(-104, 65, 447, BlockFace.WEST, 15, RenderZone.ALL);
	}

	private static void initFrame(Location location, BlockFace facing, ItemStack is, RenderZone renderZone) {
		ItemFrame frame = (ItemFrame) Metropolis.CITY_WORLD.spawnEntity(location, EntityType.ITEM_FRAME);
		frame.setFacingDirection(facing.getOppositeFace());
		frame.setItem(is);

		EntityRenderer.addItemFrame(idCounter, frame, location, facing, is, renderZone);
		idCounter++;
	}

	public static void addOldFrame(Location location, BlockFace facing, ItemStack is, RenderZone renderZone, int ID) {
		ItemFrame frame = (ItemFrame) Metropolis.CITY_WORLD.spawnEntity(location, EntityType.ITEM_FRAME);
		frame.setFacingDirection(facing.getOppositeFace());
		frame.setItem(is);
		frame.setSilent(true);
		EntityRenderer.ITEMFRAME_ID.put(ID, frame.getUniqueId());
	}

	private static void initFrame(int x, int y, int z, BlockFace facing, ItemStack is, RenderZone renderZone) {
		initFrame(new Location(Metropolis.CITY_WORLD, x, y, z), facing, is, renderZone);
	}

	private static void initMapFrame(int x, int y, int z, BlockFace facing, int mapID, RenderZone renderZone) {
		initFrame(x, y, z, facing, FILLED_MAPS[mapID], renderZone);
	}

	private static void initMapFrames(int x1, int y1, int z1, int x2, int y2, int z2, BlockFace facing, int mapID,
			RenderZone renderZone) {
		if (x1 > x2) {
			initMapFrames(x2, y1, z1, x1, y2, z2, facing, mapID, renderZone);
			return;
		}
		if (y1 > y2) {
			initMapFrames(x1, y2, z1, x2, y1, z2, facing, mapID, renderZone);
			return;
		}
		if (z1 > z2) {
			initMapFrames(x1, y1, z2, x2, y2, z1, facing, mapID, renderZone);
			return;
		}

		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				for (int z = z1; z <= z2; z++)
					initMapFrame(x, y, z, facing, mapID, renderZone);
	}
}
