package me.simon76800.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.simon76800.library.util.EntityHider;
import me.simon76800.library.util.EntityHider.Policy;
import me.simon76800.lobby.event.InventoryClickEventHandler;
import me.simon76800.lobby.event.InventoryCloseEventHandler;
import me.simon76800.lobby.event.InventoryOpenEventHandler;
import me.simon76800.lobby.event.PlayerInteractEventHandler;
import me.simon76800.lobby.event.PlayerJoinEventHandler;
import me.simon76800.lobby.util.CreateCharacter;

public class LobbyMain extends JavaPlugin {
	public static LobbyMain instance;

	public static World LOBBY_WORLD;

	public static EntityHider entityHider;

	public static PlayerInteractEventHandler playerInteractEventHandler;

	public static ArmorStand NEXT_LEFT;
	public static ArmorStand NEXT_RIGHT;
	public static ArmorStand CREATE_CHARACTER;
	public static ArmorStand ENTER_WORLD;
	public static ArmorStand DELETE_CHARACTER;

	@Override
	public void onEnable() {
		instance = this;

		LOBBY_WORLD = Bukkit.getWorld("world");

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
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		entityHider = new EntityHider(this, Policy.WHITELIST);
		playerInteractEventHandler = new PlayerInteractEventHandler();

		getServer().getPluginManager().registerEvents(new InventoryClickEventHandler(), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseEventHandler(), this);
		getServer().getPluginManager().registerEvents(new InventoryOpenEventHandler(), this);
		getServer().getPluginManager().registerEvents(playerInteractEventHandler, this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);

		CreateCharacter.init();

		NEXT_LEFT = (ArmorStand) LOBBY_WORLD.spawnEntity(new Location(LOBBY_WORLD, -467.5, 63.5, 1028.5),
				EntityType.ARMOR_STAND);
		NEXT_RIGHT = (ArmorStand) LOBBY_WORLD.spawnEntity(new Location(LOBBY_WORLD, -475.5, 63.5, 1028.5),
				EntityType.ARMOR_STAND);
		CREATE_CHARACTER = (ArmorStand) LOBBY_WORLD.spawnEntity(new Location(LOBBY_WORLD, -471.5, 63.5, 1030.2),
				EntityType.ARMOR_STAND);
		ENTER_WORLD = (ArmorStand) LOBBY_WORLD.spawnEntity(new Location(LOBBY_WORLD, -471.5, 63.5, 1030.2),
				EntityType.ARMOR_STAND);
		DELETE_CHARACTER = (ArmorStand) LOBBY_WORLD.spawnEntity(new Location(LOBBY_WORLD, -471.5, 63.5, 1026.8),
				EntityType.ARMOR_STAND);
		NEXT_LEFT.setCustomName("" + ChatColor.YELLOW + "Previous");
		NEXT_RIGHT.setCustomName("" + ChatColor.YELLOW + "Next");
		CREATE_CHARACTER.setCustomName("" + ChatColor.GREEN + "New Character");
		ENTER_WORLD.setCustomName("" + ChatColor.GREEN + "Enter World");
		DELETE_CHARACTER.setCustomName("" + ChatColor.RED + "Delete Character");

		for (Entity e : LOBBY_WORLD.getEntities()) {
			if (e instanceof ArmorStand) {
				((ArmorStand) e).setVisible(false);
				e.setGravity(false);
				e.setCustomNameVisible(true);
			}
		}
		
		/*
		 * Prevent food bar from falling
		 */
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.setSaturation(20.0F);
					p.setExhaustion(0.0F);
					p.setFoodLevel(20);
				}
			}
		}, 20L, 200L);
	}

	@Override
	public void onDisable() {
		for (Entity e : LOBBY_WORLD.getEntities()) {
			if (e instanceof ArmorStand)
				e.remove();
		}
	}
}
