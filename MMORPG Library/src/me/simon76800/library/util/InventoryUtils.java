package me.simon76800.library.util;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.PacketPlayOutHeldItemSlot;

public final class InventoryUtils {
	/**
	 * Forces player to keep selected item slot on hot bar
	 * 
	 * @param p player to affect
	 * @param slot 1-9th slot for player to highlight
	 */
	public static void forceItemSlot(Player p, int slot) {
		PacketPlayOutHeldItemSlot packet = new PacketPlayOutHeldItemSlot(slot);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
