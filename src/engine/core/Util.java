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
package engine.core;

/**
 * Various utility functions that don't have their own class.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Util {
	/**
	 * Clamps a floating point value between a specific range.
	 * <p>
	 * TODO: Perhaps make this a static method in a static Math3D class.
	 * 
	 * @param val
	 *            The value to be clamped
	 * @param min
	 *            The minimum value that val should be. If val is less than
	 *            this, then this is returned.
	 * @param max
	 *            The maximum value that val should be. If val is greater than
	 *            this, then this is returned.
	 * @return If val is within the range (min, max), then val. Otherwise, if
	 *         val is below min, min is returned. Otherwise, if val is above
	 *         max, max is returned.
	 */
	public static float clamp(float val, float min, float max) {
		if (val < min) {
			return min;
		}
		if (val > max) {
			return max;
		}

		return val;
	}

	/**
	 * Calculates the 2D vector length from two floating point values.
	 * <p>
	 * TODO: Perhaps make this a static method in a VectorMath class, or a
	 * member method in a Vector2f class.
	 * 
	 * @param x
	 *            The x value of the vector
	 * @param y
	 *            The y value of the vector
	 * @return The length of the vector defined by (x, y)
	 */
	public static float vectorLength(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Calculates one component of a vector reflection.
	 * <p>
	 * All parameters must be from the same components in the various vectors
	 * for the results to be meaningful. For instance, if vec is an x component
	 * of the initial vector, then normal should be the x component of the
	 * reflection normal. Otherwise, the result will be meaningless.
	 * <p>
	 * If called correctly, the result will be the corresponding component in
	 * the reflection vector. For instance, if vec and normal are x components,
	 * then the result will be the x component of the reflection vector.
	 * <p>
	 * TODO: Perhaps make this a static method in a VectorMath class, or a
	 * member method in a Vector2f class.
	 * 
	 * @param vec
	 *            The component of interest in the initial vector
	 * @param normal
	 *            The component of interest in the reflection normal
	 * @param dot
	 *            The dot product between the initial vector and the reflection
	 *            normal.
	 * @return The component of interest in the final reflection vector
	 */
	public static float vectorReflect(float vec, float normal,
			float dot) {
		return vec - (normal * dot * 2);
	}

	/**
	 * Performs a linear interpolation to calculate a value that is amt way
	 * between a and b.
	 * <p>
	 * For instance, if amt is one half (1/2, or 0.5), then the result will be
	 * half way between a and b. If amt is 3/4 (or 0.75), then the result will
	 * be 3/4 of the way between a and b.
	 * <p>
	 * TODO: Perhaps make this a static method in a static Math3D class.
	 * 
	 * @param a
	 *            The starting value
	 * @param b
	 *            The ending value
	 * @param amt
	 *            The amt between a and b to calculate.
	 * @return The value that is amt way between a and b.
	 */
	public static float lerp(float a, float b, float amt) {
		return (b - a) * amt + a;
	}
}
