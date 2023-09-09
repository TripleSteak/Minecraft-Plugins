package me.simon76800.library.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.simon76800.library.mob.Mob;

public final class CollisionHandler {
	/**
	 * Determines if a hitscan fired from given location will hit a mob
	 * 
	 * @param origin
	 *            the location from which the hitscan is fired (usually Player)
	 * @param mob
	 *            mob to detect collision
	 * @return the location where the mob was hit by the player
	 */
	public static Location hitscanCollision(Location origin, Mob mob) {
		double[] actualPoints = mob.actualHitboxPoints;

		double originSlope = Math.tan(-1 * origin.getYaw() * Math.PI / 180.0);
		double originXInt = origin.getX() - originSlope * origin.getZ();

		List<double[]> intersections = new ArrayList<double[]>();

		intersections.add(intersects(originSlope, originXInt, actualPoints[0], actualPoints[1], actualPoints[2],
				actualPoints[3]));
		intersections.add(intersects(originSlope, originXInt, actualPoints[4], actualPoints[5], actualPoints[2],
				actualPoints[3]));
		intersections.add(intersects(originSlope, originXInt, actualPoints[4], actualPoints[5], actualPoints[6],
				actualPoints[7]));
		intersections.add(intersects(originSlope, originXInt, actualPoints[0], actualPoints[1], actualPoints[6],
				actualPoints[7]));

		int totalIntersections = 0;
		for (double[] array : intersections) {
			if (array[0] == 1)
				totalIntersections++;
		}

		if (totalIntersections >= 2) {
			double originYZSlope = Math.tan(-1 * origin.getPitch() * Math.PI / 180.0)
					/ Math.cos(-1 * origin.getYaw() * Math.PI / 180.0);
			double originYInt = origin.getY() - originYZSlope * origin.getZ();

			if (originYZSlope == Double.NaN) { // all y-values are intersected, must have collision
				return new Location(origin.getWorld(), origin.getX(),
						(origin.getY() < mob.getLocation().getY() + mob.hitboxY[0]
								? mob.getLocation().getY() + mob.hitboxY[0]
								: mob.getLocation().getY() + mob.hitboxY[1]),
						origin.getZ());
			} else {
				// Counters for y-coordinate of intersection
				int beneathCount = 0;
				int midCount = 0;
				int aboveCount = 0;

				List<Location> intersectLocations = new ArrayList<Location>();

				for (double[] array : intersections) {
					if (array[0] == 0)
						continue;

					double yValue = getYValue(array, originYZSlope, originYInt);

					if (yValue < mob.getLocation().getY() + mob.hitboxY[0])
						beneathCount++;
					else if (yValue > mob.getLocation().getY() + mob.hitboxY[1])
						aboveCount++;
					else
						midCount++;

					intersectLocations.add(new Location(origin.getWorld(), array[1], yValue, array[2]));
				}

				int numZeroes = (beneathCount == 0 ? 1 : 0) + (midCount == 0 ? 1 : 0) + (aboveCount == 0 ? 1 : 0);
				if (midCount >= 2 || numZeroes < 2)
					return getClosest(origin, intersectLocations);
			}
		}

		return null;
	}

	/**
	 * Returns the closest location to the origin
	 * 
	 * @param origin
	 *            location to which the listed locations are compared
	 * @param list
	 *            locations to compare distance
	 * @return closest location
	 */
	private static Location getClosest(Location origin, List<Location> list) {
		Location closest = null;
		double distance = Double.MAX_VALUE;

		for (Location loc : list) {
			double locDistance = Math.sqrt(Math.pow(origin.getX() - loc.getX(), 2)
					+ Math.pow(origin.getY() - loc.getY(), 2) + Math.pow(origin.getZ() - loc.getZ(), 2));
			if (locDistance < distance) {
				closest = loc;
				distance = locDistance;
			}
		}

		return closest;
	}

	/**
	 * Calculates the y-coordinate at which the origin view intersects the hitbox
	 * 
	 * @param intersect
	 *            intersection array (3 values) from
	 *            {@link #intersects(double, double, double, double, double, double)}
	 * @param originYZSlope
	 *            slope value of origin view in YZ plane
	 * @param originYInt
	 *            y-intercept of origin view
	 * @return
	 */
	private static double getYValue(double[] intersect, double originYZSlope, double originYInt) {
		return originYZSlope * intersect[2] + originYInt;
	}

	/**
	 * Determines whether the player's line of sight intersects with the given line
	 * (plane)
	 * 
	 * @param originSlope
	 *            slope value of origin view in XZ plane
	 * @param originXInt
	 *            x-intercept of origin view
	 * @param point1X,
	 *            point1Z, point2X, point2Z endpoints of line to test intersection
	 * @return double array with size 3, { intersection (0/1), x, z }
	 */
	private static double[] intersects(double originSlope, double originXInt, double point1X, double point1Z,
			double point2X, double point2Z) {
		double[] returnArray = new double[] { 0.0, 0.0, 0.0 };

		if (point2Z - point1Z != 0) {
			double hitboxSlope = (point2X - point1X) / (point2Z - point1Z);
			double hitboxXInt = point1X - hitboxSlope * point1Z;

			if (hitboxSlope == originSlope)
				return returnArray; // if lines are parallel, no intersection

			double zValue = (hitboxXInt - originXInt) / (originSlope - hitboxSlope);

			if ((zValue >= point1Z && zValue <= point2Z) || (zValue >= point2Z && zValue <= point1Z)) {
				returnArray[0] = 1;
				returnArray[1] = hitboxSlope * zValue + hitboxXInt;
				returnArray[2] = zValue;
			}
		} else {
			if (originSlope == Double.NaN)
				return returnArray; // if lines are parallel, no intersection

			double xValue = originSlope * point2Z + originXInt;

			if ((xValue >= point1X && xValue <= point2X) || (xValue >= point2X && xValue <= point1X)) {
				returnArray[0] = 1;
				returnArray[1] = xValue;
				returnArray[2] = point2Z;
			}
		}
		return returnArray;
	}
}
