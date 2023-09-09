package me.triplesteak.metropolis.util.performance;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.jobs.JobShrine;
import me.triplesteak.metropolis.npc.NPC;
import me.triplesteak.metropolis.npc.NPCList;
import me.triplesteak.metropolis.world.ArmorStandItems;
import me.triplesteak.metropolis.world.CustomChair;
import me.triplesteak.metropolis.world.CustomChair.ChairType;
import me.triplesteak.metropolis.world.Elevator;
import me.triplesteak.metropolis.world.ItemFrames;

public final class EntityRenderer {
	public static final HashMap<RenderZone, List<Player>> RENDER_ZONES_OCCUPIED = new HashMap<>();

	/*
	 * ArmorStandItems entity management (based on IDs)
	 */
	public static final HashMap<Integer, UUID> ARMORSTANDITEM_ID = new HashMap<>();
	private static final HashMap<Integer, Location> ARMORSTANDITEM_LOC = new HashMap<>();
	private static final HashMap<Integer, ItemStack> ARMORSTANDITEM_HEAD = new HashMap<>();
	private static final HashMap<Integer, RenderZone> ARMORSTANDITEM_RENDERZONES = new HashMap<>();

	public static void addArmorStand(int ID, ArmorStand as, Location location, ItemStack headItem,
			RenderZone renderZone) {
		ARMORSTANDITEM_ID.put(ID, as.getUniqueId());
		ARMORSTANDITEM_LOC.put(ID, location);
		ARMORSTANDITEM_HEAD.put(ID, headItem);
		ARMORSTANDITEM_RENDERZONES.put(ID, renderZone);
	}

	/*
	 * NPC entity management
	 */
	public static final int NPC_RENDER_DIST = 32;

	/*
	 * CustomChair entity management (based on IDs)
	 */
	public static final HashMap<Integer, UUID> CHAIR_ID = new HashMap<>();
	private static final HashMap<Integer, Location> CHAIR_LOC = new HashMap<>();
	private static final HashMap<Integer, ChairType> CHAIR_TYPE = new HashMap<>();
	private static final HashMap<Integer, RenderZone> CHAIR_RENDERZONES = new HashMap<>();

	public static void addChair(int ID, ArmorStand as, Location location, ChairType type, RenderZone renderZone) {
		CHAIR_ID.put(ID, as.getUniqueId());
		CHAIR_LOC.put(ID, location);
		CHAIR_TYPE.put(ID, type);
		CHAIR_RENDERZONES.put(ID, renderZone);
	}

	/*
	 * ItemFrames entity management
	 */
	public static final HashMap<Integer, UUID> ITEMFRAME_ID = new HashMap<>();
	private static final HashMap<Integer, Location> ITEMFRAME_LOC = new HashMap<>();
	private static final HashMap<Integer, BlockFace> ITEMFRAME_FACING = new HashMap<>();
	private static final HashMap<Integer, ItemStack> ITEMFRAME_ITEMSTACK = new HashMap<>();
	private static final HashMap<Integer, RenderZone> ITEMFRAME_RENDERZONES = new HashMap<>();

	public static void addItemFrame(int ID, ItemFrame itemFrame, Location location, BlockFace facing, ItemStack is,
			RenderZone renderZone) {
		ITEMFRAME_ID.put(ID, itemFrame.getUniqueId());
		ITEMFRAME_LOC.put(ID, location);
		ITEMFRAME_FACING.put(ID, facing);
		ITEMFRAME_ITEMSTACK.put(ID, is);
		ITEMFRAME_RENDERZONES.put(ID, renderZone);
	}

	/**
	 * Re-establishes which areas require entity rendering
	 */
	public static void fullRenderCheck() {
		refreshRenderZoneOccupation();

		/*
		 * Armour stands
		 */
		@SuppressWarnings("unused")
		int requireCounter = 0;
		for (int ID : ARMORSTANDITEM_ID.keySet()) {
			boolean requireRender = false;
			RenderZone zone = ARMORSTANDITEM_RENDERZONES.get(ID);
			ArmorStand as = ARMORSTANDITEM_ID.get(ID) == null ? null
					: (ArmorStand) Bukkit.getEntity(ARMORSTANDITEM_ID.get(ID));
			Location asLoc = ARMORSTANDITEM_LOC.get(ID);
			ItemStack headItem = ARMORSTANDITEM_HEAD.get(ID);

			if (zone == RenderZone.ALL) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (zone.withinDistance(player.getLocation(), asLoc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			} else {
				for (Player player : RENDER_ZONES_OCCUPIED.get(zone)) {
					if (zone.withinDistance(player.getLocation(), asLoc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			}

			if (requireRender && as == null) {
				ArmorStandItems.addOldStand(asLoc, headItem, ID);
			} else if (!requireRender && as != null) {
				Bukkit.getEntity(ARMORSTANDITEM_ID.get(ID)).remove();
				ARMORSTANDITEM_ID.put(ID, null);
			}
		}
		// System.out.println("Currently rendering " + requireCounter + "/" +
		// ArmorStandItems.idCounter + " armor stand items");

		/*
		 * NPC rendering
		 */
		requireCounter = 0;
		for (NPC npc : NPCList.NPC_LIST) {
			boolean requireRender = false;

			for (Player player : npc.loc.getWorld().getPlayers()) {
				if (player.getLocation().distance(npc.loc) <= NPC_RENDER_DIST) {
					requireRender = true;
					requireCounter++;
					break;
				}
			}

			if (requireRender && !npc.alive)
				npc.spawn();
			else if (!requireRender && npc.alive)
				npc.butcher();
		}
		// System.out.println("Currently rendering " + requireCounter + "/" +
		// NPCList.NPC_LIST.size() + " NPCs");

		/*
		 * Chairs
		 */
		requireCounter = 0;
		for (int ID : CHAIR_ID.keySet()) {
			boolean requireRender = false;
			RenderZone zone = CHAIR_RENDERZONES.get(ID);
			ArmorStand as = CHAIR_ID.get(ID) == null ? null : (ArmorStand) Bukkit.getEntity(CHAIR_ID.get(ID));
			Location asLoc = CHAIR_LOC.get(ID);
			ChairType type = CHAIR_TYPE.get(ID);

			if (zone == RenderZone.ALL) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (zone.withinDistance(player.getLocation(), asLoc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			} else {
				for (Player player : RENDER_ZONES_OCCUPIED.get(zone)) {
					if (zone.withinDistance(player.getLocation(), asLoc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			}

			if (requireRender && as == null) {
				CustomChair.createOldChair(asLoc, type, ID);
			} else if (!requireRender && as != null) {
				CustomChair.destroy((ArmorStand) Bukkit.getEntity(CHAIR_ID.get(ID)));
				CHAIR_ID.put(ID, null);
			}
		}
		// System.out.println("Currently rendering " + requireCounter + "/" +
		// CustomChair.idCounter + " custom chairs");

		/*
		 * Item frame
		 */
		requireCounter = 0;
		for (int ID : ITEMFRAME_ID.keySet()) {
			boolean requireRender = false;
			RenderZone zone = ITEMFRAME_RENDERZONES.get(ID);
			ItemFrame frame = ITEMFRAME_ID.get(ID) == null ? null : (ItemFrame) Bukkit.getEntity(ITEMFRAME_ID.get(ID));
			Location loc = ITEMFRAME_LOC.get(ID);
			BlockFace facing = ITEMFRAME_FACING.get(ID);
			ItemStack is = ITEMFRAME_ITEMSTACK.get(ID);

			if (zone == RenderZone.ALL) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (zone.withinDistance(player.getLocation(), loc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			} else {
				for (Player player : RENDER_ZONES_OCCUPIED.get(zone)) {
					if (zone.withinDistance(player.getLocation(), loc)) {
						requireRender = true;
						requireCounter++;
						break;
					}
				}
			}

			if (requireRender && frame == null) {
				ItemFrames.addOldFrame(loc, facing, is, zone, ID);
			} else if (!requireRender && frame != null) {
				Bukkit.getEntity(ITEMFRAME_ID.get(ID)).remove();
				ITEMFRAME_ID.put(ID, null);
			}
		}
		// System.out.println("Currently rendering " + requireCounter + "/" +
		// ItemFrames.idCounter + " item frames");

		/*
		 * Elevator rendering
		 */
		for (Elevator elevator : Elevator.ELEVATORS) {
			boolean requireRender = false;
			RenderZone zone = elevator.renderZone;

			if (zone == RenderZone.ALL)
				requireRender = true;
			else {
				if (RENDER_ZONES_OCCUPIED.get(zone).size() > 0) {
					elevator.refreshFloorIndicators(RENDER_ZONES_OCCUPIED.get(zone));
					requireRender = true;
				}
			}

			if (requireRender && !elevator.rendered)
				elevator.initElevatorParts();
			else if (!requireRender && elevator.rendered)
				elevator.destroy();
		}

		/*
		 * Job shrine rendering
		 */
		for (JobShrine shrine : JobShrine.JOB_SHRINES) {
			shrine.updateRender(RENDER_ZONES_OCCUPIED.get(shrine.renderZone).size() > 0);
		}
	}

	/**
	 * Conducts occupation check, determines which render zones must be rendered
	 */
	public static void refreshRenderZoneOccupation() {
		for (RenderZone zone : RENDER_ZONES_OCCUPIED.keySet()) {
			RENDER_ZONES_OCCUPIED.get(zone).clear();
			for (Player player : zone.world.getPlayers()) {
				if (zone.inRenderZone(player.getLocation())) {
					RENDER_ZONES_OCCUPIED.get(zone).add(player);
					break;
				}
			}
		}
	}
}
