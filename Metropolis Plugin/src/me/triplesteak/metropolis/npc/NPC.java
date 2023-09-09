package me.triplesteak.metropolis.npc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.triplesteak.metropolis.Metropolis;

public abstract class NPC {
	public static final double GREET_DISTANCE = 3.0;

	private Villager entity;
	private ArmorStand secondName;
	
	public boolean alive;

	public final String name;
	public final Type type;
	public final Profession profession;
	public final Location loc;

	public NPC(String name, Type type, Profession profession, Location loc, boolean hasGreeting) {
		this.name = name;
		this.type = type;
		this.profession = profession;
		this.loc = loc;

		NPCList.NPC_LIST.add(this);

		if (hasGreeting) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() { // greetings timer
				@Override
				public void run() {
					for (Player player : loc.getWorld().getPlayers()) {
						if (player.getWorld() == loc.getWorld() && loc.distance(player.getLocation()) < GREET_DISTANCE)
							greet(player);
					}
				}
			}, (long) (60 + Math.random() * 100), 100);
		}
	}

	public Villager getEntity() {
		return entity;
	}

	/**
	 * Greets nearby player Method may be left empty is hasGreeting is false
	 */
	public abstract void greet(Player player);

	public void spawn() {
		entity = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		entity.setInvulnerable(true);
		entity.setCustomNameVisible(true);
		entity.setSilent(true);

		secondName = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 0.2, 0), EntityType.ARMOR_STAND);
		secondName.setCustomName(ChatColor.GREEN + name);
		secondName.setCustomNameVisible(true);
		secondName.setGravity(false);
		secondName.setInvulnerable(true);
		secondName.setVisible(false);

		if (this instanceof NPCVendor)
			entity.setCustomName(ChatColor.GRAY + "Vendor");
		else if (this instanceof NPCTeller)
			entity.setCustomName(ChatColor.GRAY + "Teller");

		entity.setVillagerType(type);
		entity.setVillagerLevel(5);
		entity.setProfession(profession);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 255, true, false));
		
		alive = true;
	}

	public void butcher() {
		if (entity != null)
			entity.remove();
		if (secondName != null)
			secondName.remove();
		
		alive = false;
	}

	public void broadcastMessage(Player player, String message) {
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "NPC " + ChatColor.RESET + ChatColor.WHITE + name
				+ " " + ChatColor.GRAY + message);
	}
}
