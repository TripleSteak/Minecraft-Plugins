package me.simon76800.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.simon76800.game.util.AttackHandler;

public class EntityDamageByEntityEventHandler implements Listener {
	@EventHandler
	public void onEvent(EntityDamageByEntityEvent e) {
		e.setCancelled(true);

		if (e.getDamager() instanceof Player)
			AttackHandler.playerLeftClickAttack((Player) e.getDamager());
	}
}
