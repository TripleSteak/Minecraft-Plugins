package me.triplesteak.metropolis.world;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.util.performance.RenderZone;
import net.minecraft.server.v1_16_R3.EntityArmorStand;

public class Elevator {
	public static final List<Elevator> ELEVATORS = new ArrayList<>();

	/*
	 * Keeps track of players who are selecting a floor
	 */
	public static final HashMap<Player, Elevator> SELECTING_FLOOR_ELEVATOR = new HashMap<>();
	public static final HashMap<Player, Integer> SELECTING_FLOOR = new HashMap<>();

	private final int buttonColumns;
	private final int totalFloors;
	private final int elevators;
	private final double speed;

	public RenderZone renderZone;
	public boolean rendered = false;

	private final String[] floorLabels;
	private final int[] floorY;

	private final Location[][] buttonLocations; // [column #][floor #]
	private final Location[][] elevatorRest; // [elevator #][floor #]

	private final BlockFace enterDirection; // direction player faces when entering the elevator
	private final ElevatorType type;

	// elevator model armour stand (two lists exist to avoid culling errors)
	private final ArrayList<ArmorStand> elevatorParts = new ArrayList<>();

	// floor display
	private ArmorStand[][] floorIndicators; // [elevator][floor #]
	private boolean[] floorIndicatorsRendered;

	// buttons to call elevator
	private final ArrayList<ItemFrame> elevatorButtons = new ArrayList<>();

	private final double[] curElevatorY; // keeps track of elevator's current altitude

	private final ArrayList<Queue<Integer>> elevatorQueue = new ArrayList<>(); // elevator request/command queue, in
																				// terms of y-coordinates

	/*
	 * References to Bukkit scheduled repeating events to operate elevators
	 */
	private final int[] idleBukkitTask;
	private final int[] activeBukkitTask;

	private final boolean[] doorsOpen;
	private final int[] curFloor;

	private final int[] openDoorTask;

	public static void initElevators() {
		/*
		 * TD Bank HQ elevator
		 */
		Elevator elevatorTDBankHQ = new Elevator(1, new int[] { -68 }, new int[] { 466 }, 1, 20,
				new String[] { "P", "L", "2", "C", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
						"16", "17", "18", "19" },
				new int[] { 56, 63, 77, 81, 89, 93, 97, 101, 105, 109, 113, 117, 121, 125, 129, 133, 137, 141, 145,
						149 },
				2, new int[] { -65, -65 }, new int[] { 465, 468 }, BlockFace.EAST, ElevatorType.GLASS_2X2, 3.6,
				RenderZone.TEEDEE_BANK);

		/*
		 * TD Bank HQ executive elevator
		 */
		Elevator elevatorTDBankHQExec = new Elevator(1, new int[] { -69 }, new int[] { 478 }, 1, 2,
				new String[] { "19", "20" }, new int[] { 149, 153 }, 1, new int[] { -66 }, new int[] { 480 },
				BlockFace.EAST, ElevatorType.GLASS_2X2, 2.7, RenderZone.TEEDEE_BANK);

		ELEVATORS.add(elevatorTDBankHQ);
		ELEVATORS.add(elevatorTDBankHQExec);
	}

	/**
	 * 
	 * @param buttonColumns   number of elevator call button columns
	 * @param buttonX         x-coordinate of button columns
	 * @param buttonZ         z-coordinate of button columns
	 * @param floorButtonDiff how many blocks above the floor is the button? (0
	 *                        means foot level)
	 * @param totalFloors     # of floors operational
	 * @param floorLabels     names of each floor, bottom up
	 * @param floorY          altitude of each floor, bottom up (y pos when standing
	 *                        on floor)
	 * @param elevators       # of elevators in use
	 * @param elevatorX       x-coordinates of elevators' centres
	 * @param elevatorZ       z-coordinates of elevators' centres
	 * @param enterDirection  direction player faces when entering the elevator
	 * @param elevatorType    type and size of elevator
	 * @param speed           speed of the elevator, in m/s (must be multiples of
	 *                        0.9 m/s)
	 */
	public Elevator(int buttonColumns, int[] buttonX, int[] buttonZ, int floorButtonDiff, int totalFloors,
			String[] floorLabels, int[] floorY, int elevators, int[] elevatorX, int[] elevatorZ,
			BlockFace enterDirection, ElevatorType type, double speed, RenderZone renderZone) {
		this.buttonColumns = buttonColumns;
		this.totalFloors = totalFloors;
		this.elevators = elevators;
		this.floorLabels = floorLabels;
		this.floorY = floorY;
		this.enterDirection = enterDirection;
		this.type = type;
		this.speed = speed;
		this.renderZone = renderZone;

		this.buttonLocations = new Location[buttonColumns][totalFloors];
		for (int i = 0; i < buttonColumns; i++) {
			for (int j = 0; j < totalFloors; j++)
				buttonLocations[i][j] = new Location(Metropolis.CITY_WORLD, buttonX[i], floorY[j] + floorButtonDiff,
						buttonZ[i]);
		}

		this.elevatorRest = new Location[elevators][totalFloors];
		for (int i = 0; i < elevators; i++) {
			for (int j = 0; j < totalFloors; j++)
				elevatorRest[i][j] = new Location(Metropolis.CITY_WORLD, elevatorX[i], floorY[j], elevatorZ[i]);
		}

		curElevatorY = new double[elevators];
		for (int i = 0; i < elevators; i++)
			curElevatorY[i] = floorY[0] - 1;

		/*
		 * Initialize elevator queues
		 */
		for (int i = 0; i < elevators; i++)
			elevatorQueue.add(new LinkedList<Integer>());

		/*
		 * Spawn doors
		 */
		for (int i = 0; i < elevators; i++)
			for (int j = 0; j < totalFloors; j++)
				spawnDoors(j, i);

		floorIndicators = new ArmorStand[elevators][totalFloors];
		floorIndicatorsRendered = new boolean[totalFloors];
		initElevatorParts(); // spawn elevators

		/*
		 * Spawn doors again
		 */
		for (int i = 0; i < elevators; i++)
			for (int j = 0; j < totalFloors; j++)
				spawnDoors(j, i);

		idleBukkitTask = new int[elevators];
		activeBukkitTask = new int[elevators];
		doorsOpen = new boolean[elevators];
		curFloor = new int[elevators];
		openDoorTask = new int[elevators];

		for (int i = 0; i < elevators; i++) {
			elevatorQueue.get(i).add(floorY[0]);
			beginIdleTask(i);
		}

		initButtons();
	}

	/**
	 * Begins idle task, periodically checking for tasks in queue
	 */
	private void beginIdleTask(int elevator) {
		Bukkit.getScheduler().cancelTask(activeBukkitTask[elevator]);

		idleBukkitTask[elevator] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				if (!doorsOpen[elevator] && !elevatorQueue.get(elevator).isEmpty())
					beginActiveTask(elevator);
			}
		}, 10L, 20L); // runs every second
	}

	/**
	 * Begins active task, moves towards next request in queue
	 */
	private void beginActiveTask(int elevator) {
		Bukkit.getScheduler().cancelTask(idleBukkitTask[elevator]);

		List<LivingEntity> entityList = new ArrayList<>();
		for (LivingEntity entity : Metropolis.CITY_WORLD.getLivingEntities()) {
			if (!(entity instanceof Shulker) && !(entity instanceof ArmorStand) && isInElevator(entity, elevator))
				entityList.add(entity);
		}

		boolean goingUp = elevatorQueue.get(elevator).peek() > curElevatorY[elevator];
		int amplifier = (int) (speed / 0.9 - 1);

		for (LivingEntity entity : entityList) // add levitation effects
			entity.removePotionEffect(PotionEffectType.LEVITATION);

		activeBukkitTask[elevator] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance,
				new Runnable() {
					int floorCheck = 5; // counts down ticks, checks at end
					boolean appliedEffects = false;

					@Override
					public void run() {
						if (floorCheck == 0) {
							int prevFloor = curFloor[elevator];
							if (curFloor[elevator] != 0
									&& Math.abs(floorY[curFloor[elevator]] - curElevatorY[elevator]) > Math
											.abs(floorY[curFloor[elevator] - 1] - curElevatorY[elevator]))
								curFloor[elevator]--;
							else if (curFloor[elevator] != totalFloors - 1
									&& Math.abs(floorY[curFloor[elevator]] - curElevatorY[elevator]) > Math
											.abs(floorY[curFloor[elevator] + 1] - curElevatorY[elevator]))
								curFloor[elevator]++;
							floorCheck = 5;

							if (prevFloor != curFloor[elevator]) { // update indicators
								for (ArmorStand as : floorIndicators[elevator]) {
									if (as != null)
										as.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD
												+ floorLabels[curFloor[elevator]]);
								}
							}
						} else
							floorCheck--;

						double targetY = elevatorQueue.get(elevator).peek();

						if (curElevatorY[elevator] != targetY) { // requires movement
							double dirMultiplier = targetY > curElevatorY[elevator] ? 1 : -1;
							double delta = speed / 20 * dirMultiplier;

							// don't overshoot
							if ((curElevatorY[elevator] - targetY) * (curElevatorY[elevator] + delta - targetY) < 0)
								delta = targetY - curElevatorY[elevator];

							// apply potion effects
							if (!appliedEffects) {
								for (LivingEntity entity : entityList) {
									entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 999999,
											goingUp ? amplifier : 254 - amplifier, true, false));
									entity.teleport(entity.getLocation().clone().add(0, 0.3, 0));
								}
								appliedEffects = true;

								for (int j = 0; j < 4; j++) { // remove floor
									double centreOffset = type.radius - 0.5;

									Location floorLoc = new Location(Metropolis.CITY_WORLD,
											elevatorRest[elevator][0].getX() + ((double) (j / 2) - centreOffset),
											floorY[curFloor[elevator]] - 1,
											elevatorRest[elevator][0].getZ() + ((double) (j % 2) - centreOffset), 0f,
											0f);
									Metropolis.CITY_WORLD.getBlockAt(floorLoc).setType(Material.AIR);
								}
							}

							// teleport elevator upward/downward
							elevatorParts.get(elevator)
									.teleport(elevatorParts.get(elevator).getLocation().clone().add(0, delta, 0));

							// update elevator altitude
							curElevatorY[elevator] += delta;
						} else {
							elevatorQueue.get(elevator).poll(); // task complete

							for (LivingEntity entity : entityList) // remove levitation effects
							{
								if (entity.hasPotionEffect(PotionEffectType.LEVITATION)) {
									entity.removePotionEffect(PotionEffectType.LEVITATION);
									entity.teleport(new Location(Metropolis.CITY_WORLD, entity.getLocation().getX(),
											curElevatorY[elevator] + 0.2, entity.getLocation().getZ(),
											entity.getLocation().getYaw(), entity.getLocation().getPitch()));
								}
							}

							for (int j = 0; j < 4; j++) { // solidify ground
								double centreOffset = type.radius - 0.5;

								Location floorLoc = new Location(Metropolis.CITY_WORLD,
										elevatorRest[elevator][0].getX() + ((double) (j / 2) - centreOffset),
										floorY[curFloor[elevator]] - 1,
										elevatorRest[elevator][0].getZ() + ((double) (j % 2) - centreOffset), 0f, 0f);
								Metropolis.CITY_WORLD.getBlockAt(floorLoc).setType(Material.BARRIER);
							}

							Bukkit.getScheduler().scheduleSyncDelayedTask(Metropolis.instance, new Runnable() {
								@Override
								public void run() {
									for (LivingEntity entity : entityList) {
										if (entity.getLocation().getY() < floorY[curFloor[elevator]])
											entity.teleport(new Location(Metropolis.CITY_WORLD,
													entity.getLocation().getX(), curElevatorY[elevator] + 0.2,
													entity.getLocation().getZ(), entity.getLocation().getYaw(),
													entity.getLocation().getPitch()));
									}

									entityList.clear();
								}
							}, 4L);

							// make sure players don't fall through

							openDoors(elevator, curFloor[elevator]);
							beginIdleTask(elevator);
						}
					}
				}, 10L, 1L); // runs every tick
	}

	/**
	 * Opens the elevator doors for 5 seconds
	 */
	private void openDoors(int elevator, int floor) {
		doorsOpen[elevator] = true;
		Metropolis.CITY_WORLD.playSound(elevatorRest[elevator][curFloor[elevator]], Sound.BLOCK_NOTE_BLOCK_CHIME, 3f,
				1f);

		elevatorParts.get(elevator).getEquipment()
				.setHelmet(ArmorStandItems.getItemStack(Material.DIAMOND_SHOVEL, type.openDoorItemDurability));

		List<EntityArmorStand> doorParts = new ArrayList<>();

		Location door1 = elevatorRest[elevator][floor].clone()
				.subtract(getPositiveVector(enterDirection, type.radius + 0.5))
				.add(getClockwiseVector(enterDirection, 0.5));
		Location door2 = elevatorRest[elevator][floor].clone()
				.subtract(getPositiveVector(enterDirection, type.radius + 0.5))
				.subtract(getClockwiseVector(enterDirection, 0.5));
		Location door3 = door1.clone().add(0, 1, 0);
		Location door4 = door2.clone().add(0, 1, 0);
		Location[] doorLocations = new Location[] { door1, door2, door3, door4 };

		for (int k = 0; k < 4; k++) {
			EntityArmorStand door = createMovingBlock(
					doorLocations[k].clone().add(getPositiveVector(enterDirection, 0.2)),
					doorLocations[k].getBlock().getBlockData());
			doorParts.add(door);
		}

		Metropolis.CITY_WORLD.getBlockAt(door1).setType(Material.AIR);
		Metropolis.CITY_WORLD.getBlockAt(door2).setType(Material.AIR);
		Metropolis.CITY_WORLD.getBlockAt(door3).setType(Material.AIR);
		Metropolis.CITY_WORLD.getBlockAt(door4).setType(Material.AIR);

		openDoorTask[elevator] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
			int tickCount = 0;
			// direction of door moving right (from outside elevator)
			Vector right = getClockwiseVector(enterDirection, 0.1);

			@Override
			public void run() {
				try {
					if (tickCount == 0) {
						Metropolis.CITY_WORLD.playSound(elevatorRest[elevator][curFloor[elevator]],
								Sound.BLOCK_PISTON_CONTRACT, 1.5f, 1f);
					} else if (tickCount <= 8) {
						for (int i = 0; i < 2; i++) {
							doorParts.get(i * 2).setPosition(doorParts.get(i * 2).locX() + right.getX(),
									doorParts.get(i * 2).locY(), doorParts.get(i * 2).locZ() + right.getZ());
							doorParts.get(i * 2 + 1).setPosition(doorParts.get(i * 2 + 1).locX() - right.getX(),
									doorParts.get(i * 2 + 1).locY(), doorParts.get(i * 2 + 1).locZ() - right.getZ());
						}
					} else if (tickCount == 109)
						Metropolis.CITY_WORLD.playSound(elevatorRest[elevator][curFloor[elevator]],
								Sound.BLOCK_PISTON_EXTEND, 1.5f, 1f);
					else if (tickCount >= 110 && tickCount < 118) {

						for (int i = 0; i < 2; i++) {
							doorParts.get(i * 2).setPosition(doorParts.get(i * 2).locX() - right.getX(),
									doorParts.get(i * 2).locY(), doorParts.get(i * 2).locZ() - right.getZ());
							doorParts.get(i * 2 + 1).setPosition(doorParts.get(i * 2 + 1).locX() + right.getX(),
									doorParts.get(i * 2 + 1).locY(), doorParts.get(i * 2 + 1).locZ() + right.getZ());
						}
					} else if (tickCount >= 118) {
						if (floorIndicators[elevator][curFloor[elevator]] != null)
							floorIndicators[elevator][curFloor[elevator]].setCustomName(
									ChatColor.GREEN + "" + ChatColor.BOLD + floorLabels[curFloor[elevator]]);
						for (EntityArmorStand as : doorParts) { // remove door entities after doors close
							for (org.bukkit.entity.Entity entity : as.getBukkitEntity().getPassengers())
								entity.remove();
							as.die();
						}

						spawnDoors(curFloor[elevator], elevator);

						elevatorParts.get(elevator).getEquipment().setHelmet(
								ArmorStandItems.getItemStack(Material.DIAMOND_SHOVEL, type.closedDoorItemDurability));

						doorsOpen[elevator] = false;
						finishOpenDoorTask(elevator);
					}

					tickCount++;
				} catch (Exception ex) {
					spawnDoors(curFloor[elevator], elevator);
				}
			}
		}, 5L, 1L); // process of open and closing doors
	}

	private void finishOpenDoorTask(int elevator) {
		Bukkit.getScheduler().cancelTask(openDoorTask[elevator]);

		/*
		 * Send floor selection screen to all players
		 */
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (isInElevator(player, elevator))
				sendSelectionScreen(player, elevator);
		}
	}

	private boolean isInElevator(org.bukkit.entity.Entity entity, int elevator) {
		int entityX = entity.getLocation().getBlockX();
		int entityZ = entity.getLocation().getBlockZ();

		int elevatorCentreX = (int) elevatorRest[elevator][0].getX();
		int elevatorCentreZ = (int) elevatorRest[elevator][0].getZ();

		boolean elevatorX = false;
		boolean elevatorZ = false;

		for (int x = 0; x < 2 * type.radius; x++) {
			for (int z = 0; z < 2 * type.radius; z++) {
				if (elevatorCentreX + (int) Math.floor(x - type.radius + 0.5) == entityX)
					elevatorX = true;
				if (elevatorCentreZ + (int) Math.floor(z - type.radius + 0.5) == entityZ)
					elevatorZ = true;
			}
		}

		return elevatorX && elevatorZ;
	}

	public static boolean isInAnyElevator(Player player) {
		for (Elevator elevator : ELEVATORS) {
			for (int i = 0; i < elevator.elevators; i++) {
				if (elevator.isInElevator(player, i))
					return true;
			}
		}
		return false;
	}

	private void sendSelectionScreen(Player player, int elevator) {
		int rows = (int) Math.ceil(((double) totalFloors) / 10.0);
		String[] floorLines = new String[rows];
		for (int i = 0; i < rows; i++) {
			floorLines[i] = "";
			for (int floor = i * 10; floor < (i + 1) * 10 && floor < totalFloors; floor++) {
				floorLines[i] += "  ";
				if (curFloor[elevator] == totalFloors - floor - 1)
					floorLines[i] += ChatColor.GREEN;
				else if (elevatorQueue.get(elevator).contains(floorY[totalFloors - floor - 1]))
					floorLines[i] += ChatColor.YELLOW;
				else
					floorLines[i] += ChatColor.GRAY;

				floorLines[i] += floorLabels[totalFloors - floor - 1];
			}
		}

		player.sendMessage("");
		player.sendMessage(ChatColor.DARK_GRAY + "===============[" + ChatColor.WHITE + " Elevator "
				+ ChatColor.DARK_GRAY + "]===============");
		for (String str : floorLines)
			player.sendMessage(str);
		player.sendMessage(ChatColor.DARK_GRAY + "========================================");
		player.sendMessage(ChatColor.GRAY + "Please type the name of the floor you wish to travel to.");

		putSelectingFloor(player, elevator);
	}

	private void putSelectingFloor(Player player, int elevator) {
		SELECTING_FLOOR.put(player, elevator);
		SELECTING_FLOOR_ELEVATOR.put(player, this);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				try {
					SELECTING_FLOOR.remove(player);
					SELECTING_FLOOR_ELEVATOR.remove(player);

				} catch (ConcurrentModificationException e) {
					// No action required
				} finally {
					if (isInElevator(player, elevator))
						putSelectingFloor(player, elevator);
				}
			}
		}, 600L);
	}

	public static boolean selectFloor(Player player, String selection) {
		if (SELECTING_FLOOR_ELEVATOR.containsKey(player)) {
			Elevator elev = SELECTING_FLOOR_ELEVATOR.get(player);
			return elev.selectFloor(player, SELECTING_FLOOR.get(player), selection);
		}
		return false;
	}

	private boolean selectFloor(Player player, int elevator, String selection) {
		int floor = -1;
		for (int i = 0; i < totalFloors; i++) {
			if (selection.equalsIgnoreCase(floorLabels[i]))
				floor = i;
		}

		if (floor == -1)
			return false;

		SELECTING_FLOOR_ELEVATOR.remove(player);
		selectFloor(elevator, floor);
		return true;
	}

	private void selectFloor(int elevator, int floor) {
		elevatorQueue.get(elevator).add(floorY[floor]);
	}

	/**
	 * Initializes elevator buttons
	 */
	@SuppressWarnings("deprecation")
	private void initButtons() {
		ItemStack buttonItem = new ItemStack(Material.FILLED_MAP);
		MapMeta map = ((MapMeta) buttonItem.getItemMeta());
		map.setMapId(ItemFrames.elevatorButtonID);
		buttonItem.setItemMeta(map);

		for (int col = 0; col < buttonColumns; col++) {
			for (int floor = 0; floor < totalFloors; floor++) {
				ItemFrame frame = (ItemFrame) Metropolis.CITY_WORLD.spawnEntity(buttonLocations[col][floor],
						EntityType.ITEM_FRAME);
				frame.setFacingDirection(enterDirection.getOppositeFace());
				frame.setItem(buttonItem.clone());

				elevatorButtons.add(frame);
			}
		}
	}

	/**
	 * Initializes and spawns elevator into world
	 * 
	 * Elevators MUST spawn one block below floor to function properly
	 */
	public void initElevatorParts() {
		for (int i = 0; i < totalFloors; i++)
			floorIndicatorsRendered[i] = true;
		rendered = true;
		
		for (int i = 0; i < elevators; i++) {
			/*
			 * Spawn elevator model
			 */
			float elevatorYaw = enterDirection == BlockFace.WEST ? 0
					: (enterDirection == BlockFace.NORTH ? 90 : (enterDirection == BlockFace.EAST ? 180 : (float) 270));

			ArmorStand as1 = (ArmorStand) elevatorRest[i][0].getWorld().spawnEntity(
					new Location(elevatorRest[i][0].getWorld(), elevatorRest[i][0].getX(), elevatorRest[i][0].getY(),
							elevatorRest[i][0].getZ(), elevatorYaw, elevatorRest[i][0].getPitch()),
					EntityType.ARMOR_STAND);
			as1.setGravity(false);
			as1.setInvulnerable(true);
			as1.setVisible(false);
			as1.setSilent(true);

			elevatorParts.add(as1);

			/*
			 * Initialize floor indicators
			 */
			Vector indicatorVector = getPositiveVector(enterDirection.getOppositeFace(), type.radius + 1.5);
			for (int j = 0; j < totalFloors; j++) {
				ArmorStand as = (ArmorStand) Metropolis.CITY_WORLD.spawnEntity(
						elevatorRest[i][j].clone().add(indicatorVector).add(0, 2, 0), EntityType.ARMOR_STAND);
				as.setInvulnerable(true);
				as.setVisible(false);
				as.setMarker(true);
				as.setGravity(false);
				as.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + floorLabels[0]);
				as.setCustomNameVisible(true);

				floorIndicators[i][j] = as;
			}
		}
	}

	public void refreshFloorIndicators(List<Player> playerList) {
		Vector indicatorVector = getPositiveVector(enterDirection.getOppositeFace(), type.radius + 1.5);
		boolean[] render = new boolean[totalFloors];
		for (int i = 0; i < totalFloors; i++)
			render[i] = false;

		for (Player player : playerList) {
			for (int i = 0; i < totalFloors; i++)
				if (renderZone.withinDistance(player.getLocation(), buttonLocations[0][i]))
					render[i] = true;
		}

		for (int floor = 0; floor < totalFloors; floor++) {
			if (render[floor] & !floorIndicatorsRendered[floor]) {
				for (int i = 0; i < elevators; i++) {
					ArmorStand as = (ArmorStand) Metropolis.CITY_WORLD.spawnEntity(
							elevatorRest[i][floor].clone().add(indicatorVector).add(0, 2, 0), EntityType.ARMOR_STAND);
					as.setInvulnerable(true);
					as.setVisible(false);
					as.setMarker(true);
					as.setGravity(false);
					as.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + floorLabels[curFloor[i]]);
					as.setCustomNameVisible(true);

					floorIndicators[i][floor] = as;
					floorIndicatorsRendered[floor] = true;
				}
			} else if (!render[floor] & floorIndicatorsRendered[floor]) {
				for (int i = 0; i < elevators; i++) {
					if (floorIndicators[i][floor] != null)
						floorIndicators[i][floor].remove();
					floorIndicators[i][floor] = null;
					floorIndicatorsRendered[floor] = false;
				}
			}
		}
	}

	/**
	 * Spawns solid elevator doors, called after elevator doors close
	 * 
	 * @param floor    starts from 0
	 * @param elevator starts from 0
	 */
	private void spawnDoors(int floor, int elevator) {
		Location door1 = elevatorRest[elevator][floor].clone()
				.subtract(getPositiveVector(enterDirection, type.radius + 0.5))
				.add(getClockwiseVector(enterDirection, 0.5));
		Location door2 = elevatorRest[elevator][floor].clone()
				.subtract(getPositiveVector(enterDirection, type.radius + 0.5))
				.subtract(getClockwiseVector(enterDirection, 0.5));

		door1.getBlock().setType(Material.AIR);
		door2.getBlock().setType(Material.AIR);
		door1.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
		door2.clone().add(0, 1, 0).getBlock().setType(Material.AIR);

		Block doorBottomBlock = door1.getBlock();
		Block doorUpBlock = doorBottomBlock.getRelative(BlockFace.UP);
		BlockData blockData = Metropolis.instance.getServer().createBlockData(Material.IRON_DOOR);
		Door data = (Door) blockData;

		data.setFacing(enterDirection.getOppositeFace());
		data.setHinge(Hinge.LEFT);
		data.setHalf(Bisected.Half.BOTTOM);
		doorBottomBlock.setBlockData(data, false);

		data.setHalf(Bisected.Half.TOP);
		doorUpBlock.setBlockData(data, false);

		doorBottomBlock = door2.getBlock();
		doorUpBlock = doorBottomBlock.getRelative(BlockFace.UP);
		blockData = Metropolis.instance.getServer().createBlockData(Material.IRON_DOOR);
		data = (Door) blockData;

		data.setFacing(enterDirection.getOppositeFace());
		data.setHinge(Hinge.RIGHT);
		data.setHalf(Bisected.Half.BOTTOM);
		doorBottomBlock.setBlockData(data, false);

		data.setHalf(Bisected.Half.TOP);
		doorUpBlock.setBlockData(data, false);
	}

	/**
	 * Button for elevator has been pressed, somewhere
	 * 
	 * @return if location is a valid button
	 */
	public static boolean requestElevator(Player player, Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		for (Elevator elevator : ELEVATORS) {
			for (int col = 0; col < elevator.buttonColumns; col++) {
				if (elevator.buttonLocations[col][0].getBlockX() == x
						&& elevator.buttonLocations[col][0].getBlockZ() == z) {
					for (int floor = 0; floor < elevator.totalFloors; floor++) {
						if (elevator.buttonLocations[col][floor].getBlockY() == y) {
							elevator.requestElevator(player, floor);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Requests an elevator at the given floor
	 */
	private void requestElevator(Player player, int floor) {
		Metropolis.CITY_WORLD.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 2f, 0.5f);

		int elevator = 0, minQueue = 10000, minFloorDiff = 10000;
		for (int i = 0; i < elevators; i++) {
			if (elevatorQueue.get(i).contains(floorY[floor])) { // already in queue
				return;
			}

			if (curFloor[i] == floor) {
				if (doorsOpen[i])
					return;
			}
		}

		for (int i = 0; i < elevators; i++) {
			if (elevatorQueue.get(i).size() < minQueue) {
				elevator = i;
				minQueue = elevatorQueue.get(i).size();
				minFloorDiff = Math.abs(curFloor[i] - floor);
			} else if (elevatorQueue.get(i).size() == minQueue) {
				if (minFloorDiff > Math.abs(curFloor[i] - floor)) {
					elevator = i;
					minFloorDiff = Math.abs(curFloor[i] - floor);
				}
			}
		}

		elevatorQueue.get(elevator).add(floorY[floor]);
	}

	/**
	 * 
	 * @return a vector of the given magnitude and direction
	 */
	private Vector getPositiveVector(BlockFace direction, double distance) {
		int normalX = direction == BlockFace.EAST ? 1 : (direction == BlockFace.WEST ? -1 : 0);
		int normalZ = direction == BlockFace.SOUTH ? 1 : (direction == BlockFace.NORTH ? -1 : 0);

		return new Vector(distance * normalX, 0, distance * normalZ);
	}

	private Vector getClockwiseVector(BlockFace direction, double distance) {
		switch (direction) {
		case NORTH:
			direction = BlockFace.EAST;
			break;
		case EAST:
			direction = BlockFace.SOUTH;
			break;
		case SOUTH:
			direction = BlockFace.WEST;
			break;
		case WEST:
			direction = BlockFace.NORTH;
			break;
		default:
			break;
		}
		return getPositiveVector(direction, distance);
	}

	/**
	 * Creates a moving block that rests on an invisible armour stand
	 */
	private EntityArmorStand createMovingBlock(Location loc, BlockData data) {
		EntityArmorStand entityAS = new EntityArmorStand(((CraftWorld) Metropolis.CITY_WORLD).getHandle(), loc.getX(),
				loc.getY(), loc.getZ());

		entityAS.setInvulnerable(true);
		entityAS.setInvisible(true);
		entityAS.setNoGravity(true);
		entityAS.setMarker(true);

		entityAS.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld) Metropolis.CITY_WORLD).getHandle().addEntity(entityAS, SpawnReason.CUSTOM);

		FallingBlock fallingBlock = (FallingBlock) Metropolis.CITY_WORLD.spawnFallingBlock(loc, data);
		fallingBlock.setDropItem(false);
		fallingBlock.setInvulnerable(true);
		fallingBlock.setGravity(false);

		(entityAS.getBukkitEntity()).addPassenger(fallingBlock);

		return entityAS;
	}

	public static void destroyAll() {
		for (Elevator elevator : ELEVATORS)
			elevator.destroy();
	}

	public void destroy() {
		for (int i = 0; i < elevators; i++)
			destroy(i);

		elevatorParts.clear();

		for (int i = 0; i < elevators; i++) {
			for (int j = 0; j < totalFloors; j++) {
				if (floorIndicators[i][j] != null) {
					floorIndicators[i][j].remove();
					floorIndicators[i][j] = null;
				}
			}
		}

		rendered = false;
	}

	private void destroy(int elevator) {
		for (int j = 0; j < 4; j++) { // solidify ground
			double centreOffset = type.radius - 0.5;

			Location floorLoc = new Location(Metropolis.CITY_WORLD,
					elevatorRest[elevator][0].getX() + ((double) (j / 2) - centreOffset),
					floorY[curFloor[elevator]] - 1,
					elevatorRest[elevator][0].getZ() + ((double) (j % 2) - centreOffset), 0f, 0f);
			Metropolis.CITY_WORLD.getBlockAt(floorLoc).setType(Material.AIR);
		}

		curFloor[elevator] = 0;
		curElevatorY[elevator] = floorY[0] - 1;

		try {
			elevatorParts.get(elevator).remove();
		} catch (Exception ex) {
		}
	}

	public enum ElevatorType {
		GLASS_2X2(1.0, 3.0, 6, 7);

		public double radius;
		public double interiorHeight;
		public int openDoorItemDurability;
		public int closedDoorItemDurability;

		ElevatorType(double radius, double interiorHeight, int openDoorItemDurability, int closedDoorItemDurability) {
			this.radius = radius;
			this.interiorHeight = interiorHeight;
			this.openDoorItemDurability = openDoorItemDurability;
			this.closedDoorItemDurability = closedDoorItemDurability;
		}
	}
}
