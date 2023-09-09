package me.simon76800.library;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.util.EntityHider;
import me.simon76800.library.util.EntityHider.Policy;

public final class InvisibilityToggle {
	private static EntityHider entityHider;

	/*
	 * List of entities invisible to everyone
	 */
	public static final List<Entity> TOTAL_BLACKLIST = new ArrayList<>();

	/*
	 * List of entities only invisible to a certain faction
	 */
	public static final List<Entity> LUMINOUS_BLACKLIST = new ArrayList<>();
	public static final List<Entity> OBSCURE_BLACKLIST = new ArrayList<>();

	public static void init() {
		entityHider = new EntityHider(Main.instance, Policy.BLACKLIST);
	}

	/**
	 * Prevents any luminous players from seeing the entity
	 * 
	 * @param e
	 *            entity to blacklist
	 */
	public static void addLuminousBlacklist(Entity e) {
		LUMINOUS_BLACKLIST.add(e);

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (PlayerCharacter.getPlayerCharacter(p).race.isLuminous)
				entityHider.hideEntity(p, e);
		}
	}

	/**
	 * Prevents any obscure players from seeing the entity
	 * 
	 * @param e
	 *            entity to blacklist
	 */
	public static void addObscureBlacklist(Entity e) {
		OBSCURE_BLACKLIST.add(e);

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!PlayerCharacter.getPlayerCharacter(p).race.isLuminous)
				entityHider.hideEntity(p, e);
		}
	}

	/**
	 * Prevents any players from seeing the entity
	 * 
	 * @param e
	 *            entity to blacklist
	 */
	public static void addTotalBlacklist(Entity e) {
		TOTAL_BLACKLIST.add(e);

		for (Player p : Bukkit.getOnlinePlayers()) {
			entityHider.hideEntity(p, e);
		}
	}

	/**
	 * Masks entities from newly joined player
	 * 
	 * @param p
	 *            player who joined
	 */
	public static void newPlayerJoined(Player p) {
		for (Entity e : TOTAL_BLACKLIST)
			entityHider.hideEntity(p, e);

		if (PlayerCharacter.getPlayerCharacter(p).race.isLuminous) {
			for (Entity e : LUMINOUS_BLACKLIST)
				entityHider.hideEntity(p, e);
		} else
			for (Entity e : OBSCURE_BLACKLIST)
				entityHider.hideEntity(p, e);
	}
}
