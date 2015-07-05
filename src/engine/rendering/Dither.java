/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

/**
 * Holds various functions used for Dithering.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Dither {
	private static final int[] dither = new int[] { 1, 49, 13, 61, 4, 52, 16,
		64, 33, 17, 45, 29, 36, 20, 48, 32, 9, 57, 5, 53, 12, 60, 8, 56,
		41, 25, 37, 21, 44, 28, 40, 24, 3, 51, 15, 63, 2, 50, 14, 62, 35,
		19, 47, 31, 34, 18, 46, 30, 11, 59, 7, 55, 10, 58, 6, 54, 43, 27,
		39, 23, 42, 26, 38, 22, };

	/**
	 * Get the amount of dither for Bayer-Dithering a 2D coordinate.
	 * 
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @return The dither amount for this coordinate between 0.0 and 1.0.
	 */
	public static double getDither(int x, int y) {
		return dither[(x & 7) + (y & 7) * 8] / 65.0;
	}
}
