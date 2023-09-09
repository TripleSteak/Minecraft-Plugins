package me.simon76800.lobby.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.simon76800.lobby.LobbyMain;
import me.simon76800.lobby.util.CharacterManager;
import me.simon76800.lobby.util.CreateCharacter;

public class PlayerJoinEventHandler implements Listener {
	@EventHandler
	public void onEvent(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(LobbyMain.instance, new Runnable() {
			@Override
			public void run() {
				e.getPlayer().setGameMode(GameMode.ADVENTURE);
				e.getPlayer().getInventory().clear();
				e.getPlayer().setLevel(0);
				e.getPlayer().setExp(0.0F);
				e.getPlayer().teleport(new Location(LobbyMain.LOBBY_WORLD, -471.5, 64, 1028.5));

				hideAllPlayers();

				LobbyMain.entityHider.showEntity(e.getPlayer(), LobbyMain.NEXT_LEFT);
				LobbyMain.entityHider.showEntity(e.getPlayer(), LobbyMain.NEXT_RIGHT);

				CharacterManager.setup(e.getPlayer());
				CharacterManager.loadCharacters(e.getPlayer());

				CreateCharacter.updateInventory(e.getPlayer());

				PlayerInteractEventHandler.CURRENT_OPTION.put(e.getPlayer(), 0);

				ArmorStand a1 = (ArmorStand) LobbyMain.LOBBY_WORLD
						.spawnEntity(new Location(LobbyMain.LOBBY_WORLD, -471.5, 66.4, 1032.5), EntityType.ARMOR_STAND);
				ArmorStand a2 = (ArmorStand) LobbyMain.LOBBY_WORLD
						.spawnEntity(new Location(LobbyMain.LOBBY_WORLD, -471.5, 66.1, 1032.5), EntityType.ARMOR_STAND);
				a1.setVisible(false);
				a2.setVisible(false);
				a1.setGravity(false);
				a2.setGravity(false);
				LobbyMain.entityHider.showEntity(e.getPlayer(), a1);
				LobbyMain.entityHider.showEntity(e.getPlayer(), a2);
				PlayerInteractEventHandler.LEVEL_STANDS.put(e.getPlayer(), a1);
				PlayerInteractEventHandler.RACE_STANDS.put(e.getPlayer(), a2);
				Bukkit.getScheduler().scheduleSyncDelayedTask(LobbyMain.instance, new Runnable() {
					@Override
					public void run() {
						LobbyMain.playerInteractEventHandler.refreshArmorStands(e.getPlayer());
					}
				}, 30L);
			}
		}, 1L);
	}

	/**
	 * Hides all players from each other
	 */
	@SuppressWarnings("deprecation")
	private void hideAllPlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				if (p != p2)
					p.hidePlayer(p2);
			}
		}
	}
}
