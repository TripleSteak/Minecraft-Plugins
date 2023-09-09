package me.simon76800.library.item.weapon;

import org.bukkit.entity.Player;

import me.simon76800.library.item.ItemQuality;

public class RangedWeapon extends Weapon {
	public RangedWeapon(int durability, String displayName, ItemQuality quality,
			WeaponType weaponType, int levelMin, int minDamage, int maxDamage, double attackSpeed, double attackRange) {
		super(durability, displayName, quality, weaponType, levelMin, minDamage, maxDamage, attackSpeed,
				attackRange);
	}

	@Override
	public void basicAttack(Player p) {

	}
}
