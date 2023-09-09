package me.triplesteak.metropolis.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.triplesteak.metropolis.Metropolis;
import ru.beykerykt.lightapi.LightAPI;
import ru.beykerykt.lightapi.LightType;
import ru.beykerykt.lightapi.chunks.ChunkInfo;

public final class TrafficLights {
	public static final long YELLOW_DURATION = 60L; // duration of yellow light
	public static final long ALL_RED_DURATION = 60L; // duration of all red light

	private static final List<TrafficLights> LIGHTS = new ArrayList<>();
	private static final List<Location> LIGHT_LOCATIONS = new ArrayList<>();
	private static final Set<ChunkInfo> AFFECTED_CHUNKS = new HashSet<>();

	private static final ItemStack RED_LIGHT = ArmorStandItems.getItemStack(Material.DIAMOND_SHOVEL, 3);
	private static final ItemStack YELLOW_LIGHT = ArmorStandItems.getItemStack(Material.DIAMOND_SHOVEL, 4);
	private static final ItemStack GREEN_LIGHT = ArmorStandItems.getItemStack(Material.DIAMOND_SHOVEL, 5);

	private int sets;
	private long[] driveDuration;
	private long[] walkDuration;

	private List<List<ArmorStand>> lightStands = new ArrayList<>();

	public static void initTrafficLights() {
		/*
		 * The name of traffic light instances is formatted as follows:
		 * 
		 * [SET 0 STREET]_X_[SET 1 STREET]_X_[SET 2 STREET]_... etc.
		 */
		TrafficLights Main_X_Mango = new TrafficLights(2, new long[] { 800L, 600L }, new long[] { 600L, 300L });
		Main_X_Mango.addTrafficLight(0, -107.5, 68, 497, 90F);
		Main_X_Mango.addTrafficLight(0, -107.5, 68, 507, 90F);
		Main_X_Mango.addTrafficLight(0, -107.5, 68, 510, 90F);
		Main_X_Mango.addTrafficLight(0, -107.5, 68, 520, 90F);
		Main_X_Mango.addTrafficLight(0, -123.5, 68, 497, -90F);
		Main_X_Mango.addTrafficLight(0, -123.5, 68, 507, -90F);
		Main_X_Mango.addTrafficLight(0, -123.5, 68, 510, -90F);
		Main_X_Mango.addTrafficLight(0, -123.5, 68, 520, -90F);
		Main_X_Mango.addTrafficLight(1, -110, 68, 494.5, 0F);
		Main_X_Mango.addTrafficLight(1, -121, 68, 494.5, 0F);
		Main_X_Mango.addTrafficLight(1, -110, 68, 522.5, 180F);
		Main_X_Mango.addTrafficLight(1, -121, 68, 522.5, 180F);

		startTrafficProcess(); // begin traffic light circulation
		initLighting();
	}

	public static void disableTrafficLights() {
		for (Location loc : LIGHT_LOCATIONS)
			LightAPI.deleteLight(loc, LightType.BLOCK, true);
	}

	/**
	 * Update chunks to ensure lighting shows
	 */
	private static void initLighting() {
		for (Location loc : LIGHT_LOCATIONS)
			AFFECTED_CHUNKS.addAll(LightAPI.collectChunks(loc, LightType.BLOCK, 15));

		for (ChunkInfo chunk : AFFECTED_CHUNKS)
			LightAPI.updateChunk(chunk, LightType.BLOCK);
	}

	private static void startTrafficProcess() {
		for (TrafficLights light : LIGHTS) {
			for (int i = 0; i < light.sets; i++)
				for (ArmorStand as : light.lightStands.get(i))
					as.getEquipment().setHelmet(RED_LIGHT);

			Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
				/*
				 * (0):		GREEN
				 * (1):		PEDESTRIAN FINISHED
				 * (2):		YELLOW
				 * (3):		ALL RED
				 */
				private int curStage = 3;
				private int curSet = light.sets - 1;

				private long timer = ALL_RED_DURATION;

				@Override
				public void run() {
					timer -= 10L;

					if (timer <= 0) {
						if (curStage < 3) { // before set termination
							switch (curStage) {
							case 0: // pedestrian walk ending
								// switch pedestrian sign from "walk" to "flashing stop"

								timer = light.driveDuration[curSet] - light.walkDuration[curSet];
								break;
							case 1: // green light ending
								for (ArmorStand as : light.lightStands.get(curSet))
									as.getEquipment().setHelmet(YELLOW_LIGHT);

								// switch pedestrian sign from "flashing stop" to "stop"

								timer = YELLOW_DURATION;
								break;
							case 2: // yellow light ending
								for (ArmorStand as : light.lightStands.get(curSet))
									as.getEquipment().setHelmet(RED_LIGHT);

								timer = ALL_RED_DURATION;
								break;
							}

							curStage++;
						} else { // switch sets
							curStage = 0;
							curSet = (curSet + 1) % light.sets;

							for (ArmorStand as : light.lightStands.get(curSet))
								as.getEquipment().setHelmet(GREEN_LIGHT);

							// switch pedestrian sign from "stop" to "walk"

							timer = light.walkDuration[curSet];
						}
					}
				}
			}, 10L, 10L);
		}
	}

	/**
	 * Initializes a new system of traffic lights
	 * 
	 * @param sets				number of traffic light directions (usually 2)
	 * @param driveDuration		drive duration for each set, in TICKS
	 * @param walkDuration		walk duration for each set, in TICKs
	 */
	public TrafficLights(int sets, long[] driveDuration, long[] walkDuration) {
		this.sets = sets;
		this.driveDuration = driveDuration;
		this.walkDuration = walkDuration;

		for (int i = 0; i < sets; i++)
			lightStands.add(new ArrayList<>());

		LIGHTS.add(this);
	}

	private void addTrafficLight(int set, Location loc) {
		ArmorStand as = (ArmorStand) Metropolis.CITY_WORLD.spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.setVisible(false);
		as.setSilent(true);
		lightStands.get(set).add(as);

		LIGHT_LOCATIONS.add(loc);
		LightAPI.createLight(loc, LightType.BLOCK, 15, false);
	}

	private void addTrafficLight(int set, double x, double y, double z, float yaw) {
		Location loc = new Location(Metropolis.CITY_WORLD, x, y, z, yaw, 0.0F);
		addTrafficLight(set, loc);
	}
}
