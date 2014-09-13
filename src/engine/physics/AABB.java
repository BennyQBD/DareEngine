/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.physics;

/**
 * A 2D axis-aligned bounding box.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class AABB {
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

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
	public AABB(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	/**
	 * Determines if this AABB intersects another one.
	 * 
	 * @param other
	 *            The AABB to test against.
	 * @return Whether or not this AABB intersects other.
	 */
	public boolean intersectAABB(AABB other) {
		return intersectRect(other.getMinX(), other.getMinY(),
				other.getMaxX(), other.getMaxY());
	}

	/**
	 * Determines if this AABB intersects a 2D rectangle with known extents.
	 * 
	 * @param minX
	 *            The minimum extent of the rectangle on X.
	 * @param minY
	 *            The minimum extent of the rectangle on Y.
	 * @param maxX
	 *            The maximum extent of the rectangle on X.
	 * @param maxY
	 *            The maximum extent of the rectangle on Y.
	 * @return Whether or not this AABB intersects the rectangle.
	 */
	public boolean intersectRect(float minX, float minY,
			float maxX, float maxY) {
		return this.minX < maxX && this.maxX > minX
				&& this.minY < maxY && this.maxY > minY;
	}

	/**
	 * Calculates the center of this AABB on the X axis.
	 * 
	 * @return The center location of this AABB on the X axis.
	 */
	public float getCenterX() {
		return (minX + maxX) / 2.0f;
	}

	/**
	 * Calculates the center of this AABB on the Y axis.
	 * 
	 * @return The center location of this AABB on the Y axis.
	 */
	public float getCenterY() {
		return (minY + maxY) / 2.0f;
	}

	/**
	 * Calculates the distance between this AABB and another on the X axis.
	 * 
	 * @param other
	 *            The AABB which is being checked for distance.
	 * @return The distance on the X axis.
	 */
	public float getDistanceX(AABB other) {
		float distance1 = other.getMinX() - getMaxX();
		float distance2 = getMinX() - other.getMaxX();

		float distance = distance1 > distance2 ? distance1
				: distance2;
		return distance;
	}

	/**
	 * Calculates the distance between this AABB and another on the Y axis.
	 * 
	 * @param other
	 *            The AABB which is being checked for distance.
	 * @return The distance on the Y axis.
	 */
	public float getDistanceY(AABB other) {
		float distance1 = other.getMinY() - getMaxY();
		float distance2 = getMinY() - other.getMaxY();

		float distance = distance1 > distance2 ? distance1
				: distance2;
		return distance;
	}

	/**
	 * Gets the minimum extent of this AABB on the X axis.
	 * 
	 * @return The minimum extent of this AABB on the X axis.
	 */
	public float getMinX() {
		return minX;
	}

	/**
	 * Gets the minimum extent of this AABB on the Y axis.
	 * 
	 * @return The minimum extent of this AABB on the Y axis.
	 */
	public float getMinY() {
		return minY;
	}

	/**
	 * Gets the maximum extent of this AABB on the X axis.
	 * 
	 * @return The maximum extent of this AABB on the X axis.
	 */
	public float getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum extent of this AABB on the Y axis.
	 * 
	 * @return The maximum extent of this AABB on the Y axis.
	 */
	public float getMaxY() {
		return maxY;
	}
}
