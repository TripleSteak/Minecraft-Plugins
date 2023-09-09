package me.simon76800.game;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.simon76800.game.command.SpawnMobCommand;
import me.simon76800.game.event.EventManager;
import me.simon76800.game.util.MobUpdater;
import me.simon76800.library.map.Dimension;

public class GameMain extends JavaPlugin {
	public static GameMain instance;
	public static EventManager em;

	@Override
	public void onEnable() {
		instance = this;
		em = new EventManager(this);
		
		this.getCommand("spawn_mob").setExecutor(new SpawnMobCommand());

		Bukkit.createWorld(new WorldCreator(Dimension.ESDORFIA_MAIN.name));

		/*
		 * Initialize world characteristics
		 */
		for (World w : Bukkit.getWorlds()) {
			w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			w.setGameRule(GameRule.DO_FIRE_TICK, false);
			w.setGameRule(GameRule.DO_MOB_LOOT, false);
			w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
			w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			w.setGameRule(GameRule.KEEP_INVENTORY, false);
			w.setGameRule(GameRule.MOB_GRIEFING, false);
			w.setGameRule(GameRule.NATURAL_REGENERATION, false);
			w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
			
			System.out.println("Successfully initialized world " + w.getName());
			
			for (Entity e : w.getEntities())
				e.remove();
		}
		
		/*
		 * Prevent depleting saturation
		 */
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.setSaturation(20.0F);
					p.setExhaustion(0.0F);
				}
			}
		}, 20L, 200L);
		
		/*
		 * Begin mob update loop
		 */
		MobUpdater.updateLoop();
	}

	@Override
	public void onDisable() {

	}
}
