package me.triplesteak.metropolis.world;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.triplesteak.metropolis.util.BlockUtils;
import me.triplesteak.metropolis.util.MaterialUtils;
import me.triplesteak.metropolis.world.CustomChair.ChairType;

/**
 * Chairs derived from vanilla Minecraft blocks
 */
public final class Chair {
	public static final HashMap<Player, ArmorStand> ACTIVE_SITTING = new HashMap<>();

	/**
	 * Will return -1 if the clicked block is not part of a chair. Otherwise:
	 * 
	 * (0) Carpet in piston-carpet chair
	 * (1) Piston in piston-carpet chair
	 * (2) Snow in snow-wool chair
	 * (3) Slab in slab-trapdoor chair
	 * (4) Trapdoor in slab-trapdoor chair
	 * (5) Toilet
	 * (6) Stairs-sign chair
	 * (7) Stairs in stairs-wood couch
	 * (8) Slab in stairs-wood couch
	 */
	public static int isChair(Block block) {
		if (MaterialUtils.isCarpet(block)) { // carpet in piston-carpet chair
			Block pistonHead = block.getRelative(0, -1, 0);
			if (pistonHead.getType() == Material.PISTON_HEAD
					&& ((PistonHead) pistonHead.getBlockData()).getFacing() == BlockFace.UP)
				return 0;
		} else if (block.getType() == Material.PISTON_HEAD
				&& ((PistonHead) block.getBlockData()).getFacing() == BlockFace.UP) {
			// piston in piston-carpet chair
			if (MaterialUtils.isCarpet(block.getRelative(0, 1, 0)))
				return 1;
		} else if (block.getType() == Material.SNOW) {
			// snow wool chair
			if (BlockUtils.adjacentToWool(block, 1))
				return 2;
		} else if (MaterialUtils.isSlab(block)) { // slab in slab-trapdoor chair
			if (BlockUtils.adjacentToTrapdoor(block, 3))
				return 3;
		} else if (MaterialUtils.isTrapDoor(block)) { // trapdoor in slab-trapdoor chair
			boolean flag = false;

			for (int x = -1; x <= 1; x++) { // check if trapdoor is next to slab
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && z == 0)
						continue;
					if (MaterialUtils.isBottomSlab(block.getRelative(x, 0, z))
							&& BlockUtils.adjacentToTrapdoor(block.getRelative(x, 0, z), 3))
						flag = true;
				}
			}

			if (!flag) { // check if trapdoor is above chair
				if (MaterialUtils.isTrapDoor(block.getRelative(0, -1, 0))) {
					for (int x = -1; x <= 1; x++) { // check if trapdoor is next to slab
						for (int z = -1; z <= 1; z++) {
							if (x == 0 && z == 0)
								continue;
							if (MaterialUtils.isBottomSlab(block.getRelative(x, -1, z))
									&& BlockUtils.adjacentToTrapdoor(block.getRelative(x, -1, z), 3))
								flag = true;
						}
					}
				}
			}
			if (flag)
				return 4;
		} else if (block.getType() == Material.SMOOTH_QUARTZ_STAIRS) { // toilet
			Stairs stairs = (Stairs) block.getBlockData();
			if (stairs.getHalf() == Half.TOP) {

				Block otherPart = block.getRelative(stairs.getFacing());
				if (otherPart.getType() == Material.SMOOTH_QUARTZ_STAIRS) {

					Stairs otherStairs = (Stairs) otherPart.getBlockData();
					if (!(otherStairs.getHalf() == Half.BOTTOM
							|| otherStairs.getFacing() != stairs.getFacing().getOppositeFace())) {
						if (otherPart.getRelative(0, 1, 0).getType() == Material.SMOOTH_QUARTZ)
							return 5;
					}
				}
			}
		}

		if (MaterialUtils.isStairs(block)) {
			BlockFace stairsFacing = ((Stairs) block.getBlockData()).getFacing();
			if (((Stairs) block.getBlockData()).getHalf() == Half.BOTTOM) {
				if (MaterialUtils.isWallSign(block.getRelative(BlockUtils.getCWFace(stairsFacing)))
						&& ((WallSign) block.getRelative(BlockUtils.getCWFace(stairsFacing)).getBlockData())
								.getFacing() == BlockUtils.getCWFace(stairsFacing))
					return 6;
				if (MaterialUtils.isWallSign(block.getRelative(BlockUtils.getCCWFace(stairsFacing)))
						&& ((WallSign) block.getRelative(BlockUtils.getCCWFace(stairsFacing)).getBlockData())
								.getFacing() == BlockUtils.getCCWFace(stairsFacing))
					return 6;

				if (MaterialUtils.isStrippedWood(block.getRelative(BlockUtils.getCWFace(stairsFacing)))
						|| MaterialUtils.isStrippedWood(block.getRelative(BlockUtils.getCCWFace(stairsFacing)))) {
					Block nextBlock = block.getRelative(stairsFacing.getOppositeFace());
					while (MaterialUtils.isStairs(nextBlock) || (MaterialUtils.isSlab(nextBlock)
							&& ((Slab) nextBlock.getBlockData()).getType() == Type.BOTTOM)) {
						if (MaterialUtils.isStairs(nextBlock)
								&& ((Stairs) nextBlock.getBlockData()).getHalf() == Half.BOTTOM)
							return 7;
						nextBlock = nextBlock.getRelative(stairsFacing.getOppositeFace());
					}
				}
			}
		} else if (MaterialUtils.isSlab(block)) {
			BlockFace[] check = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST };

			for (int i = 0; i < 2; i++) {
				if (MaterialUtils.isStrippedWood(block.getRelative(BlockUtils.getCWFace(check[i])))
						|| MaterialUtils.isStrippedWood(block.getRelative(BlockUtils.getCCWFace(check[i])))) {
					Block nextBlock = block.getRelative(check[i]);
					boolean hasEnd = false;
					while (MaterialUtils.isStairs(nextBlock) || (MaterialUtils.isSlab(nextBlock)
							&& ((Slab) nextBlock.getBlockData()).getType() == Type.BOTTOM)) {
						if (MaterialUtils.isStairs(nextBlock)
								&& ((Stairs) nextBlock.getBlockData()).getHalf() == Half.BOTTOM) {
							hasEnd = true;
							break;
						}
						nextBlock = nextBlock.getRelative(check[i]);
					}

					if (hasEnd) {
						nextBlock = block.getRelative(check[i].getOppositeFace());
						while (MaterialUtils.isStairs(nextBlock) || (MaterialUtils.isSlab(nextBlock)
								&& ((Slab) nextBlock.getBlockData()).getType() == Type.BOTTOM)) {
							if (MaterialUtils.isStairs(nextBlock)
									&& ((Stairs) nextBlock.getBlockData()).getHalf() == Half.BOTTOM)
								return 8;
							nextBlock = nextBlock.getRelative(check[i].getOppositeFace());
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Player will attempt to sit on custom chair (chair must be valid)
	 */
	public static void sit(Player player, ArmorStand chair, ChairType type) {
		Location chairLoc = chair.getLocation().clone().add(0, type.sitOffset - 1.7, 0);

		ArmorStand as = (ArmorStand) chairLoc.getWorld().spawnEntity(chairLoc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.setVisible(false);

		as.addPassenger(player);
		ACTIVE_SITTING.put(player, as);
	}

	/**
	 * Player will attempt to sit on the chair (chair must be valid)
	 * 
	 * @param chairType return value of isChair()
	 */
	public static void sit(Player player, Block block, int chairType) {
		if (!ACTIVE_SITTING.containsKey(player)) { // player isn't currently sitting
			Location chairLoc = block.getLocation().clone().add(0.5, -0.7, 0.5);

			switch (chairType) { // adjust position based on chair type
			case 0: // carpet in piston-carpet chair
				chairLoc.subtract(0, 1, 0);
				break;
			case 1: // piston in piston-carpet chair
				break;
			case 2: // snow in snow-wool chair
				chairLoc.subtract(0, 0.4, 0);
				break;
			case 3: // slab in slab-trapdoor chair
				chairLoc.subtract(0, 0.5, 0);
				break;
			case 4: // trapdoor in slab-trapdoor chair
				for (int x = -1; x <= 1; x++) { // check if trapdoor is next to slab
					for (int z = -1; z <= 1; z++) {
						if (x == 0 && z == 0)
							continue;
						if (MaterialUtils.isBottomSlab(block.getRelative(x, 0, z))
								&& BlockUtils.adjacentToTrapdoor(block.getRelative(x, 0, z), 3))
							chairLoc.add(x, -0.5, z);
					}
				}

				if (MaterialUtils.isTrapDoor(block.getRelative(0, -1, 0))) {
					for (int x = -1; x <= 1; x++) { // check if trapdoor is next to slab
						for (int z = -1; z <= 1; z++) {
							if (x == 0 && z == 0)
								continue;
							if (MaterialUtils.isBottomSlab(block.getRelative(x, -1, z))
									&& BlockUtils.adjacentToTrapdoor(block.getRelative(x, -1, z), 3))
								chairLoc.add(x, -1.5, z);
						}
					}
				}

				break;
			case 5: // toilet
				break;
			case 6: // slab-sign chair
				chairLoc.subtract(0, 0.5, 0);
				break;
			case 7:
				Vector direction = ((Stairs) block.getBlockData()).getFacing().getOppositeFace().getDirection();
				chairLoc.add(direction.getX() / 2, -0.5, direction.getZ() / 2);
				break;
			case 8:
				chairLoc.subtract(0, 0.5, 0);
				break;
			}

			ArmorStand as = (ArmorStand) chairLoc.getWorld().spawnEntity(chairLoc, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setInvulnerable(true);
			as.setVisible(false);

			as.addPassenger(player);
			ACTIVE_SITTING.put(player, as);
		}
	}

	/**
	 * 
	 * @return if dismount was successful
	 */
	public static boolean dismount(Player player) {
		if (ACTIVE_SITTING.containsKey(player)) {
			ACTIVE_SITTING.get(player).remove();
			ACTIVE_SITTING.remove(player);
			return true;
		}
		return false;
	}
}
