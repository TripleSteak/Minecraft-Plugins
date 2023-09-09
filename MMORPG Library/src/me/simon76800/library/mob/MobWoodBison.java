package me.simon76800.library.mob;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

public class MobWoodBison extends ConstructedMob {
	private ArmorStand headAS;
	private ArmorStand bodyAS;
	private ArmorStand frontLimbsAS;
	private ArmorStand backLimbsAS;

	private Slime collisionSlime;

	// Predetermined vector offsets, {x, y, z, yaw}
	private static final double[] headOffset = new double[] { 0, -5 / 16.0, 13.0 / 16.0, 0 };
	private static final double[] bodyOffset = new double[] { 0, -5 / 16.0, 1.3125 / 16.0, 0 };
	private static final double[] frontLimbOffset = new double[] { 0, -7.3 / 16.0, 5.5 / 16.0, 0 };
	private static final double[] backLimbOffset = new double[] { 0, -7.3 / 16.0, -1, 0 };

	private ItemStack headIS;
	private ItemStack bodyIS;
	private ItemStack limbsIS;

	public MobWoodBison(Location location) {
		super("Wood Bison", location, Hostility.NEUTRAL, Hostility.NEUTRAL, 2, 15, 2.0,
				new double[] { 0.5, 0.5, -0.5, -0.5 }, new double[] { 0, 33 / 16.0 },
				new double[] { 12.5 / 16.0, -19.5 / 16.0, -19.5 / 16.0, 12.5 / 16.0 });
	}

	@Override
	protected void initItems() {
		headIS = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta headMeta = headIS.getItemMeta();
		((Damageable) headMeta).setDamage(1);
		headIS.setItemMeta(headMeta);

		bodyIS = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta bodyMeta = bodyIS.getItemMeta();
		((Damageable) bodyMeta).setDamage(2);
		bodyIS.setItemMeta(bodyMeta);

		limbsIS = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta limbsMeta = limbsIS.getItemMeta();
		((Damageable) limbsMeta).setDamage(3);
		limbsIS.setItemMeta(limbsMeta);
	}

	@Override
	protected void construct() {
		headAS = getArmorStand(
				(ArmorStand) location.getWorld().spawnEntity(getAbsolute(headOffset), EntityType.ARMOR_STAND));
		headAS.getEquipment().setHelmet(headIS);
		headAS.setHeadPose(new EulerAngle(0.0F, Math.PI / 2, 0.0F));

		bodyAS = getArmorStand(
				(ArmorStand) location.getWorld().spawnEntity(getAbsolute(bodyOffset), EntityType.ARMOR_STAND));
		bodyAS.getEquipment().setHelmet(bodyIS);
		bodyAS.setHeadPose(new EulerAngle(0.0F, Math.PI / 2, 0.0F));

		frontLimbsAS = getArmorStand(
				(ArmorStand) location.getWorld().spawnEntity(getAbsolute(frontLimbOffset), EntityType.ARMOR_STAND));
		frontLimbsAS.getEquipment().setItemInMainHand(limbsIS);
		frontLimbsAS.getEquipment().setItemInOffHand(limbsIS);
		frontLimbsAS.setArms(true);
		frontLimbsAS.setRightArmPose(new EulerAngle(0.0F, 0.0F, 0.0F));
		frontLimbsAS.setLeftArmPose(new EulerAngle(0.0F, 0.0F, 0.0F));

		backLimbsAS = getArmorStand(
				(ArmorStand) location.getWorld().spawnEntity(getAbsolute(backLimbOffset), EntityType.ARMOR_STAND));
		backLimbsAS.getEquipment().setItemInMainHand(limbsIS);
		backLimbsAS.getEquipment().setItemInOffHand(limbsIS);
		backLimbsAS.setArms(true);
		backLimbsAS.setRightArmPose(new EulerAngle(0.0F, 0.0F, 0.0F));
		backLimbsAS.setLeftArmPose(new EulerAngle(0.0F, 0.0F, 0.0F));

		collisionSlime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
		collisionSlime.setSize(4);
		collisionSlime.setAI(false);
		collisionSlime.setSilent(true);
		collisionSlime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 1, true, false));
		collisionSlime.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 99999999, 1, true, false));
		collisionSlime.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 99999999, 1, true, false));

		teleportTo(location); // fixes teleportation bug, for some reason... must be called
	}

	@Override
	public void teleportTo(Location location) {
		super.teleportTo(location);

		headAS.teleport(getAbsolute(headOffset));
		bodyAS.teleport(getAbsolute(bodyOffset));
		frontLimbsAS.teleport(getAbsolute(frontLimbOffset));
		backLimbsAS.teleport(getAbsolute(backLimbOffset));

		collisionSlime.teleport(location);
	}
}
