package me.triplesteak.metropolis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.triplesteak.metropolis.command.PlayerHeadCommand;
import me.triplesteak.metropolis.command.WalletCommand;
import me.triplesteak.metropolis.events.APCEHandler;
import me.triplesteak.metropolis.events.EDBEEHandler;
import me.triplesteak.metropolis.events.EDEHandler;
import me.triplesteak.metropolis.events.EPIEHandler;
import me.triplesteak.metropolis.events.HBBEEHandler;
import me.triplesteak.metropolis.events.ICEHandler;
import me.triplesteak.metropolis.events.PCPEHandler;
import me.triplesteak.metropolis.events.PDIEHandler;
import me.triplesteak.metropolis.events.PIAEEHandler;
import me.triplesteak.metropolis.events.PIEEHandler;
import me.triplesteak.metropolis.events.PIEHandler;
import me.triplesteak.metropolis.events.PJEHandler;
import me.triplesteak.metropolis.events.SCEHandler;
import me.triplesteak.metropolis.jobs.JobShrine;
import me.triplesteak.metropolis.npc.NPCList;
import me.triplesteak.metropolis.savedata.BankData;
import me.triplesteak.metropolis.util.TimeManager;
import me.triplesteak.metropolis.util.performance.EntityRenderer;
import me.triplesteak.metropolis.world.ArmorStandItems;
import me.triplesteak.metropolis.world.CustomChair;
import me.triplesteak.metropolis.world.Elevator;
import me.triplesteak.metropolis.world.ItemFrames;
import me.triplesteak.metropolis.world.TrafficLights;

public class Metropolis extends JavaPlugin {
	public static Metropolis instance;

	public static World CITY_WORLD;

	@Override
	public void onEnable() {
		instance = this;

		CITY_WORLD = Bukkit.getServer().createWorld(new WorldCreator("world"));

		/*
		 * World features (NPCs, still items, etc.)
		 */
		NPCList.initNPCs();
		ArmorStandItems.initStands();
		CustomChair.init();

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				ItemFrames.initItemFrames();
				Elevator.initElevators();
				TrafficLights.initTrafficLights();
				JobShrine.init();
			}
		}, 120L); // post-load

		registerEventHandlers();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (LivingEntity e : CITY_WORLD.getLivingEntities())
					e.setCollidable(false);
			}
		}, 120L, 600L); // disable collision

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				EntityRenderer.fullRenderCheck();
			}
		}, 200L, 60L); // optimized entity rendering

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				saveData();
			}
		}, 600L, 600L); // periodic saving
		
		TimeManager.initDayCycle(); // start the day cycle

		this.getCommand("playerhead").setExecutor(new PlayerHeadCommand());
		this.getCommand("wallet").setExecutor(new WalletCommand());
	}

	@Override
	public void onDisable() {
		TrafficLights.disableTrafficLights();

		for (Player player : Bukkit.getOnlinePlayers())
			player.kickPlayer(ChatColor.YELLOW + "Server restarting!");
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kill @e"); // kill everything

		Elevator.destroyAll();
		saveData();
	}

	private void registerEventHandlers() {
		registerEventHandler(new APCEHandler());
		registerEventHandler(new EDBEEHandler());
		registerEventHandler(new EDEHandler());
		registerEventHandler(new EPIEHandler());
		registerEventHandler(new HBBEEHandler());
		registerEventHandler(new ICEHandler());
		registerEventHandler(new PCPEHandler());
		registerEventHandler(new PDIEHandler());
		registerEventHandler(new PIAEEHandler());
		registerEventHandler(new PIEEHandler());
		registerEventHandler(new PIEHandler());
		registerEventHandler(new PJEHandler());
		registerEventHandler(new SCEHandler());
	}

	private void registerEventHandler(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public void saveData() {
		/*
		 * Save data
		 */
		//System.out.println("Saving player data...");
		try {
			BankData.saveAll();
		} catch (NoClassDefFoundError e) {
			System.out.println("An unexpected error occured while trying to save data.");
		}
	}
}
