package me.simon76800.library.item.weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.simon76800.library.item.Item;
import me.simon76800.library.item.ItemQuality;
import me.simon76800.library.mob.Mob;
import me.simon76800.library.mob.player.Classes;
import me.simon76800.library.mob.player.PlayerCharacter;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public abstract class Weapon extends Item { // TODO Add "Shtiv" weapon
	public ItemQuality quality;
	public WeaponType weaponType;
	public int levelMin;

	public int minDamage;
	public int maxDamage;
	public double attackSpeed;
	public double attackRange;

	public int bonusStrength;
	public int bonusIntellect;
	public int bonusConstitution;
	public int bonusStamina;
	public int bonusAgility;

	public Weapon(int durability, String displayName, ItemQuality quality, WeaponType weaponType,
			int levelMin, int minDamage, int maxDamage, double attackSpeed, double attackRange) {
		super(weaponType.material, (short) durability, displayName);
		this.quality = quality;
		this.weaponType = weaponType;
		this.levelMin = levelMin;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.attackSpeed = attackSpeed;
		this.attackRange = attackRange;
	}

	/**
	 * Simple basic attack from player (usually left click)
	 * 
	 * @param p
	 *            player who made the attack
	 */
	public abstract void basicAttack(Player p);

	/**
	 * Calculates how much damage this weapon does when given player basic attacks
	 * given mob
	 * 
	 * @param p
	 *            player using the weapon
	 * @param mob
	 *            mob being attacked by the weapon
	 * @return amount of final damage dealt
	 */
	protected int calculateBasicDamage(Player p, Mob mob) {
		double initialDamage = Math.random() * (maxDamage - minDamage + 1) + minDamage;

		return (int) initialDamage; // TODO add modifications from characteristics, implement armor/defences
	}

	/**
	 * 
	 * @param p
	 *            player using the weapon
	 * @return ItemStack version of weapon
	 */
	public ItemStack getItemStack(Player p) {
		ItemStack is = new ItemStack(super.material, 1);

		ItemMeta meta = is.getItemMeta();
		((Damageable) meta).setDamage(super.durability);
		meta.setDisplayName("" + quality.displayColour + ChatColor.BOLD + super.displayName);

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" " + ChatColor.DARK_GRAY + quality.getCapitalizedName() + " " + weaponType.getCapitalizedName());
		lore.add(" " + ChatColor.DARK_GRAY + "Minimum Level "
				+ (PlayerCharacter.getPlayerCharacter(p).level >= levelMin ? "" : ChatColor.RED) + levelMin);
		lore.add("");
		lore.add(" " + ChatColor.WHITE + minDamage + " - " + maxDamage + ChatColor.GRAY + " Damage");
		lore.add(" " + ChatColor.WHITE + (Math.round(attackSpeed * 100.0) / 100.0) + ChatColor.GRAY + " Attack Speed");

		meta.setUnbreakable(true);
		meta.setLore(lore);
		is.setItemMeta(meta);

		net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		if (!tag.hasKey("HideFlags"))
			tag.setInt("HideFlags", 63);
		nmsStack.setTag(tag);
		is = CraftItemStack.asCraftMirror(nmsStack);
		return is;
	}

	/**
	 * 
	 * @param classs
	 *            the class to test
	 * @return whether the given class is allowed to use the weapon
	 */
	public boolean classAllowed(Classes classs) {
		if (weaponType.classList.contains(classs))
			return true;
		return false;
	}

	public enum WeaponType {
		DAGGER("dagger", Arrays.asList(Classes.ASSASSIN), true, Material.DIAMOND_SWORD);

		public String name;
		public List<Classes> classList;
		public boolean isMelee;
		public Material material;

		private WeaponType(String name, List<Classes> classList, boolean isMelee, Material material) {
			this.name = name;
			this.classList = classList;
			this.isMelee = isMelee;
			this.material = material;
		}

		public String getCapitalizedName() {
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		public static WeaponType getFromName(String s) {
			for (WeaponType type : WeaponType.values()) {
				if (type.name.equalsIgnoreCase(s))
					return type;
			}
			return null;
		}
	}
}
