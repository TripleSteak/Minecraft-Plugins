package me.simon76800.game.event;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventHandler implements Listener {
	@EventHandler
	public void onEvent(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.setCancelled(true);
	}
}
