package me.triplesteak.metropolis.item.playerhead;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagString;


public enum LegacyPlayerHead {
	CHEST("MHF_Chest"), NOLAN("Psycopomp"), WATER("emack0714");

	private ItemStack is;

	LegacyPlayerHead(String playerName) {
		ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
		net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);

		NBTTagCompound compound = new NBTTagCompound();
		nmsStack.setTag(compound);
		compound = nmsStack.getTag();

		compound.set("SkullOwner", NBTTagString.a(playerName));
		nmsStack.setTag(compound);

		this.is = CraftItemStack.asBukkitCopy(nmsStack);
	}

	public ItemStack getItemStack() {
		return is;
	}
}
