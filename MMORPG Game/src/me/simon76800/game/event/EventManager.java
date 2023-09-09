package me.simon76800.game.event;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventManager {
	private Plugin plugin;

	public BlockBreakEventHandler blockBreakEvent = new BlockBreakEventHandler();
	public EntityDamageByEntityEventHandler entityDamageByEntityEvent = new EntityDamageByEntityEventHandler();
	public PlayerArmorStandManipulateEventHandler playerArmorStandManipulateEvent = new PlayerArmorStandManipulateEventHandler();
	public PlayerInteractEventHandler playerInteractEvent = new PlayerInteractEventHandler();
	public PlayerInteractEntityEventHandler playerInteractEntityEvent = new PlayerInteractEntityEventHandler();
	public PlayerJoinEventHandler playerJoinEvent = new PlayerJoinEventHandler();
	public PlayerLoginEventHandler playerLoginEvent = new PlayerLoginEventHandler();

	public EventManager(Plugin plugin) {
		this.plugin = plugin;

		register(blockBreakEvent);
		register(entityDamageByEntityEvent);
		register(playerArmorStandManipulateEvent);
		register(playerInteractEntityEvent);
		register(playerInteractEvent);
		register(playerJoinEvent);
		register(playerLoginEvent);
	}

	private void register(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
}
