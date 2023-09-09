package me.triplesteak.metropolis.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.events.EPIEHandler;
import me.triplesteak.metropolis.util.BoundingRegion;
import me.triplesteak.metropolis.util.performance.RenderZone;

public class JobShrine {
	public static final List<JobShrine> JOB_SHRINES = new ArrayList<>();

	public final List<BoundingRegion> regions;
	public final RenderZone renderZone;
	public final int jobCooldownMin; // in real life seconds
	public final int jobCooldownMax; // in real life seconds

	public final List<Job> availableJobs = new ArrayList<>();
	public final List<Double> meanHourlyWages = new ArrayList<>();
	public final List<Double> wageStandardDeviations = new ArrayList<>();

	public int activeJobIndex;
	public double activeHourlyWage;

	private ArmorStand jobTitleArmorStand;
	private ArmorStand wageArmorStand;
	private Item bookItem;
	private Location curShrineLoc;

	public static void init() {
		JobShrine TEEDEE_BANK_HQ = new JobShrine(Arrays.asList(new BoundingRegion(-91, 95, 466, -91, 151, 466), new BoundingRegion(-91, 95, 470, -91, 151, 470), new BoundingRegion(-91, 95, 473, -91, 151, 473), new BoundingRegion(-91, 95, 477, -91, 151, 477)), RenderZone.TEEDEE_BANK, 60, 300);
		TEEDEE_BANK_HQ.addJob(Job.BANK_CUSTOMER_SERVICE, 18, 2);

		updatePositions();
	}

	/**
	 * Confined to a specific area, job shrines appear in a random location with a random job within the job list.
	 * Should a player interact with the job shrine, they will be offered a chance to earn some extra cash.
	 * Upon job completion (or failure), the job shrine disappears.
	 * 
	 * Shrines will never spawn in solid ground, and will always be subjected to gravity.
	 */
	public JobShrine(List<BoundingRegion> regions, RenderZone renderZone, int jobCooldownMin,
			int jobCooldownMax) {
		this.regions = regions;
		this.renderZone = renderZone;
		this.jobCooldownMin = jobCooldownMin;
		this.jobCooldownMax = jobCooldownMax;

		JOB_SHRINES.add(this);

		scheduleNextSpawn();
	}

	public void addJob(Job job, double meanHourlyPay, double standardDeviation) {
		availableJobs.add(job);
		meanHourlyWages.add(meanHourlyPay);
		wageStandardDeviations.add(standardDeviation);
	}

	public void scheduleNextSpawn() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				generateNewJob();
			}
		}, (long) (Math.random() * (jobCooldownMax - jobCooldownMin) + jobCooldownMin) * 20L);
	}

	/**
	 * Repositions the shrine and generates a new job
	 */
	public void generateNewJob() {
		activeJobIndex = (int) Math.random() * availableJobs.size();

		Random rand = new Random();
		activeHourlyWage = rand.nextGaussian() * wageStandardDeviations.get(activeJobIndex)
				+ meanHourlyWages.get(activeJobIndex);
		if (activeHourlyWage < meanHourlyWages.get(activeJobIndex) * 0.5)
			activeHourlyWage = meanHourlyWages.get(activeJobIndex) * 0.5;
		else if (activeHourlyWage > meanHourlyWages.get(activeJobIndex) * 3)
			activeHourlyWage = meanHourlyWages.get(activeJobIndex) * 3;
		activeHourlyWage = Math.round(activeHourlyWage * 100.0) / 100.0;

		do {
			int regionNum = (int) (Math.random() * regions.size());
			curShrineLoc = regions.get(regionNum).generateRandomWithin(Metropolis.CITY_WORLD);
		} while (Metropolis.CITY_WORLD.getBlockAt(curShrineLoc).getType().isSolid());

		System.out.println(
				"New job shrine \"" + availableJobs.get(activeJobIndex).jobTitle + "\" spawned at " + curShrineLoc);
	}

	public void updateRender(boolean needRender) {
		if (needRender && jobTitleArmorStand == null && curShrineLoc != null)
			initEntities();
		else if (!needRender && jobTitleArmorStand != null)
			despawnEntities();
	}

	public void initEntities() {
		ItemStack dropItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta meta = dropItem.getItemMeta();
		meta.setLore(Arrays.asList(EPIEHandler.CANNOT_BE_PICKED));
		dropItem.setItemMeta(meta);

		bookItem = curShrineLoc.getWorld().dropItem(curShrineLoc, dropItem);

		jobTitleArmorStand = (ArmorStand) curShrineLoc.getWorld().spawnEntity(curShrineLoc.clone().add(0, 0.6, 0), EntityType.ARMOR_STAND);
		jobTitleArmorStand.setMarker(true);
		jobTitleArmorStand.setSilent(true);
		jobTitleArmorStand.setInvulnerable(true);
		jobTitleArmorStand.setVisible(false);
		jobTitleArmorStand.setGravity(false);
		jobTitleArmorStand
				.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + availableJobs.get(activeJobIndex).jobTitle);
		jobTitleArmorStand.setCustomNameVisible(true);

		wageArmorStand = (ArmorStand) curShrineLoc.getWorld().spawnEntity(curShrineLoc.clone().add(0, 0.4, 0),
				EntityType.ARMOR_STAND);
		wageArmorStand.setMarker(true);
		wageArmorStand.setSilent(true);
		wageArmorStand.setInvulnerable(true);
		wageArmorStand.setVisible(false);
		wageArmorStand.setGravity(false);
		wageArmorStand.setCustomName(ChatColor.YELLOW + "$" + activeHourlyWage + "/hour");
		wageArmorStand.setCustomNameVisible(true);
	}

	public void despawnEntities() {
		jobTitleArmorStand.remove();
		wageArmorStand.remove();
		bookItem.remove();

		jobTitleArmorStand = null;
		wageArmorStand = null;
		bookItem = null;
	}

	private static void updatePositions() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				for (JobShrine shrine : JOB_SHRINES) {
					if (shrine.bookItem != null && shrine.bookItem.getLocation().distance(shrine.curShrineLoc) > 0.05) { // teleportation
																															// necessasary
						shrine.curShrineLoc = shrine.bookItem.getLocation();
						shrine.jobTitleArmorStand.teleport(shrine.curShrineLoc.clone().add(0, 0.6, 0));
						shrine.wageArmorStand.teleport(shrine.curShrineLoc.clone().add(0, 0.4, 0));
					}
				}
			}
		}, 20L, 20L);
	}
}
