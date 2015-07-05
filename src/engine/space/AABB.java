/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.space;

/**
 * A 3D axis-aligned bounding box.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class AABB {
	private static final double DEFAULT_MIN_Z = Double.NEGATIVE_INFINITY;
	private static final double DEFAULT_MAX_Z = Double.POSITIVE_INFINITY;

	private final double minX;
	private final double minY;
	private final double minZ;
	private final double maxX;
	private final double maxY;
	private final double maxZ;

	/**
	 * Creates a new AABB based on it's extents.
	 * 
	 * @param minX
	 *            The minimum extent of the box on X.
	 * @param minY
	 *            The minimum extent of the box on Y.
	 * @param maxX
	 *            The maximum extent of the box on X.
	 * @param maxY
	 *            The maximum extent of the box on Y.
	 */
	public AABB(double minX, double minY, double maxX, double maxY) {
		this(minX, minY, DEFAULT_MIN_Z, maxX, maxY);
	}

	/**
	 * Creates a new AABB based on it's extents.
	 * 
	 * @param minX
	 *            The minimum extent of the box on X.
	 * @param minY
	 *            The minimum extent of the box on Y.
	 * @param minZ
	 *            The minimum extent of the box on Z.
	 * @param maxX
	 *            The maximum extent of the box on X.
	 * @param maxY
	 *            The maximum extent of the box on Y.
	 */
	public AABB(double minX, double minY, double minZ, double maxX, double maxY) {
		this(minX, minY, minZ, maxX, maxY, DEFAULT_MAX_Z);
	}

	/**
	 * Creates a new AABB based on it's extents.
	 * 
	 * @param minX
	 *            The minimum extent of the box on X.
	 * @param minY
	 *            The minimum extent of the box on Y.
	 * @param minZ
	 *            The minimum extent of the box on Z.
	 * @param maxX
	 *            The maximum extent of the box on X.
	 * @param maxY
	 *            The maximum extent of the box on Y.
	 * @param maxZ
	 *            The maximum extent of the box on Z.
	 */
	public AABB(double minX, double minY, double minZ, double maxX,
			double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	/**
	 * Tests whether another AABB is completely contained by this one.
	 * 
	 * @param other
	 *            The AABB being tested for containment
	 * @return True if {@code other} is contained by this AABB, false otherwise.
	 */
	public boolean contains(AABB other) {
		return minX <= other.minX && other.maxX <= maxX && minY <= other.minY
				&& other.maxY <= maxY && minZ <= other.minZ
				&& other.maxZ <= maxZ;
	}

	/**
	 * Tests whether another AABB is intersecting this one.
	 * 
	 * @param other
	 *            The AABB being tested for intersection
	 * @return True if {@code other} is intersecting this AABB, false otherwise.
	 */
	public boolean intersects(AABB other) {
		return intersectCube(other.getMinX(), other.getMinY(), other.getMinZ(),
				other.getMaxX(), other.getMaxY(), other.getMaxZ());
	}

	/**
	 * Tests whether this AABB intersects a rectangle.
	 * 
	 * @param minX
	 *            The minimum extent of the rectangle on X.
	 * @param minY
	 *            The minimum extent of the rectangle on Y.
	 * @param maxX
	 *            The maximum extent of the rectangle on X.
	 * @param maxY
	 *            The maximum extent of the rectangle on Y.
	 * @return True if the rectangle intersects this AABB, false otherwise.
	 */
	public boolean intersectRect(double minX, double minY, double maxX,
			double maxY) {
		return intersectCube(minX, minY, DEFAULT_MIN_Z, maxX, maxY,
				DEFAULT_MAX_Z);
	}

	/**
	 * Tests whether this AABB intersects a cube.
	 * 
	 * @param minX
	 *            The minimum extent of the cube on X.
	 * @param minY
	 *            The minimum extent of the cube on Y.
	 * @param minZ
	 *            The minimum extent of the cube on Z.
	 * @param maxX
	 *            The maximum extent of the cube on X.
	 * @param maxY
	 *            The maximum extent of the cube on Y.
	 * @param maxZ
	 *            The maximum extent of the cube on Z.
	 * @return True if the cube intersects this AABB, false otherwise.
	 */
	public boolean intersectCube(double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ) {
		return this.minX < maxX && this.maxX > minX && this.minY < maxY
				&& this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ;
	}

	/**
	 * Adjusts a movement amount on X so that after the move is performed, this
	 * AABB will not intersect {@code other}.
	 * <p/>
	 * This method assumes that this AABB can actually intersect {@code other}
	 * after some amount of movement on x, even if it won't necessarily
	 * intersect it after the movement specified by {@code moveAmtX}.
	 * 
	 * @param other
	 *            The AABB that this AABB is resolving against.
	 * @param moveAmtX
	 *            The amount this AABB is trying to move.
	 * @return The new, adjusted move amount that guarantees no intersection.
	 */
	public double resolveCollisionX(AABB other, double moveAmtX) {
		double newAmtX = moveAmtX;
		if (moveAmtX == 0.0) {
			return moveAmtX;
		}
		if (moveAmtX > 0) {
			// Our max == their min
			newAmtX = other.getMinX() - maxX;
		} else {
			// Our min == their max
			newAmtX = other.getMaxX() - minX;
		}

		if (Math.abs(newAmtX) < Math.abs(moveAmtX)) {
			moveAmtX = newAmtX;
		}
		return moveAmtX;
	}

	/**
	 * Adjusts a movement amount on Y so that after the move is performed, this
	 * AABB will not intersect {@code other}.
	 * <p/>
	 * This method assumes that this AABB can actually intersect {@code other}
	 * after some amount of movement on y, even if it won't necessarily
	 * intersect it after the movement specified by {@code moveAmtY}.
	 * 
	 * @param other
	 *            The AABB that this AABB is resolving against.
	 * @param moveAmtY
	 *            The amount this AABB is trying to move.
	 * @return The new, adjusted move amount that guarantees no intersection.
	 */
	public double resolveCollisionY(AABB other, double moveAmtY) {
		double newAmtY = moveAmtY;
		if (moveAmtY == 0.0) {
			return moveAmtY;
		}
		if (moveAmtY > 0) {
			// Our max == their min
			newAmtY = other.getMinY() - maxY;
		} else {
			// Our min == their max
			newAmtY = other.getMaxY() - minY;
		}

		if (Math.abs(newAmtY) < Math.abs(moveAmtY)) {
			moveAmtY = newAmtY;
		}
		return moveAmtY;
	}

	/**
	 * Calculates the center of this AABB on the X axis.
	 * 
	 * @return The center location of this AABB on the X axis.
	 */
	public double getCenterX() {
		return (minX + maxX) / 2.0;
	}

	/**
	 * Calculates the center of this AABB on the Y axis.
	 * 
	 * @return The center location of this AABB on the Y axis.
	 */
	public double getCenterY() {
		return (minY + maxY) / 2.0;
	}

	/**
	 * Calculates the center of this AABB on the Z axis.
	 * 
	 * @return The center location of this AABB on the Z axis.
	 */
	public double getCenterZ() {
		return (minZ + maxZ) / 2.0;
	}

	/**
	 * Gets the minimum extent of this AABB on the X axis.
	 * 
	 * @return The minimum extent of this AABB on the X axis.
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * Gets the minimum extent of this AABB on the Y axis.
	 * 
	 * @return The minimum extent of this AABB on the Y axis.
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * Gets the minimum extent of this AABB on the Z axis.
	 * 
	 * @return The minimum extent of this AABB on the Z axis.
	 */
	public double getMinZ() {
		return minZ;
	}

	/**
	 * Gets the width of this AABB.
	 * 
	 * @return The width of this AABB.
	 */
	public double getWidth() {
		return maxX - minX;
	}

	/**
	 * Gets the height of this AABB.
	 * 
	 * @return The height of this AABB.
	 */
	public double getHeight() {
		return maxY - minY;
	}

	/**
	 * Gets the depth of this AABB.
	 * 
	 * @return The depth of this AABB.
	 */
	public double getDepth() {
		return maxZ - minZ;
	}

	/**
	 * Gets the maximum extent of this AABB on the X axis.
	 * 
	 * @return The maximum extent of this AABB on the X axis.
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum extent of this AABB on the Y axis.
	 * 
	 * @return The maximum extent of this AABB on the Y axis.
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * Gets the maximum extent of this AABB on the Z axis.
	 * 
	 * @return The maximum extent of this AABB on the Z axis.
	 */
	public double getMaxZ() {
		return maxZ;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + maxX + "), " + "(" + minY + ", " + maxY
				+ "), " + "(" + minZ + ", " + maxZ + ")";
	}

	/**
	 * Creates an AABB equivalent to this, but in a new position.
	 * 
	 * @param amtX
	 *            The amount to move on X.
	 * @param amtY
	 *            The amount to move on Y.
	 * @return An AABB equivalent to this, but in a new position.
	 */
	public AABB move(double amtX, double amtY) {
		return new AABB(minX + amtX, minY + amtY, minZ, maxX + amtX, maxY
				+ amtY, maxZ);
	}

	/**
	 * Creates an AABB that bounds both this AABB and another AABB.
	 * 
	 * @param other
	 *            The other AABB being bounded.
	 * @return An AABB that bounds both this AABB and {@code other}.
	 */
	public AABB combine(AABB other) {
		double minX = Math.min(this.minX, other.getMinX());
		double minY = Math.min(this.minY, other.getMinY());
		double minZ = Math.min(this.minZ, other.getMinZ());
		double maxX = Math.max(this.maxX, other.getMaxX());
		double maxY = Math.max(this.maxY, other.getMaxY());
		double maxZ = Math.max(this.maxZ, other.getMaxZ());
		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Creates a new AABB equivalent to this, but stretched by a certain amount.
	 * 
	 * @param amtX
	 *            The amount to stretch on X
	 * @param amtY
	 *            The amount to stretch on Y.
	 * @return A new AABB, stretched by the specified amounts.
	 */
	public AABB stretch(double amtX, double amtY) {
		double minX, maxX, minY, maxY;
		if (amtX < 0) {
			minX = this.minX + amtX;
			maxX = this.maxX;
		} else {
			minX = this.minX;
			maxX = this.maxX + amtX;
		}
		if (amtY < 0) {
			minY = this.minY + amtY;
			maxY = this.maxY;
		} else {
			minY = this.minY;
			maxY = this.maxY + amtY;
		}
		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Creates a new AABB equivalent to this, but scaled away from the origin by
	 * a certain amount.
	 * 
	 * @param scaleX
	 *            Amount to scale on X.
	 * @param scaleY
	 *            Amount to scale on Y.
	 * @param scaleZ
	 *            Amount to scale on Z.
	 * @return A new AABB, scaled by the specified amounts.
	 */
	public AABB scale(double scaleX, double scaleY, double scaleZ) {
		return new AABB(minX * scaleX, minY * scaleY, minZ * scaleZ, maxX
				* scaleX, maxY * scaleY, maxZ * scaleZ);
	}
}
