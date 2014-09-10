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

public class AABB {
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

	public AABB(float xMin, float yMin, float xMax, float yMax) {
		this.minX = xMin;
		this.minY = yMin;
		this.maxX = xMax;
		this.maxY = yMax;
	}

	public boolean intersectAABB(AABB other) {
		return intersectRect(other.getMinX(), other.getMinY(),
				other.getMaxX(), other.getMaxY());
	}

	public boolean intersectRect(float minX, float minY,
			float maxX, float maxY) {
		return this.minX < maxX && this.maxX > minX
				&& this.minY < maxY && this.maxY > minY;
	}

	public float getCenterX() {
		return (minX + maxX) / 2.0f;
	}

	public float getCenterY() {
		return (minY + maxY) / 2.0f;
	}

	public float getDistanceX(AABB other) {
		float distance1 = other.getMinX() - getMaxX();
		float distance2 = getMinX() - other.getMaxX();

		float distance = distance1 > distance2 ? distance1
				: distance2;
		return distance;
	}

	public float getDistanceY(AABB other) {
		float distance1 = other.getMinY() - getMaxY();
		float distance2 = getMinY() - other.getMaxY();

		float distance = distance1 > distance2 ? distance1
				: distance2;
		return distance;
	}

	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}
}
