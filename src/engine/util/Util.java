/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util;

import java.util.StringTokenizer;

/**
 * Holds various utility functions
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Util {
	/**
	 * Converts an array of Integers to an array of ints.
	 * 
	 * @param src
	 *            The Integer array to convert
	 * @return An equivalent array of ints.
	 */
	public static int[] toInt(Integer[] src) {
		int[] result = new int[src.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = src[i];
		}
		return result;
	}

	/**
	 * Ensures {@code val} is in the range of 0.0 to 1.0. If {@code val} is
	 * greater than 1.0, this will return 1.0. If {@code val} is less than 0.0,
	 * this will return 0.0. Otherwise, {@code val} is returned unchanged.
	 * 
	 * @param val
	 *            The value to saturate
	 * @return {@code val}, saturated between 0.0 and 1.0.
	 */
	public static double saturate(double val) {
		return clamp(val, 0.0, 1.0);
	}

	/**
	 * Ensures {@code val} is in the range of {@code min} to {@code max}. If
	 * {@code val} is greater than {@code max}, this will return {@code max}. If
	 * {@code val} is less than {@code min}, this will return {@code min}.
	 * Otherwise, {@code val} is returned unchanged.
	 * 
	 * @param val
	 *            The value to calmp.
	 * @param min
	 *            The smallest value of the result.
	 * @param max
	 *            The largerst value of the result.
	 * @return {@code val}, clamped between {@code min} and {@code max}.
	 */
	public static int clamp(int val, int min, int max) {
		if (val < min) {
			val = min;
		} else if (val > max) {
			val = max;
		}
		return val;
	}

	/**
	 * Ensures {@code val} is in the range of {@code min} to {@code max}. If
	 * {@code val} is greater than {@code max}, this will return {@code max}. If
	 * {@code val} is less than {@code min}, this will return {@code min}.
	 * Otherwise, {@code val} is returned unchanged.
	 * 
	 * @param val
	 *            The value to calmp.
	 * @param min
	 *            The smallest value of the result.
	 * @param max
	 *            The largerst value of the result.
	 * @return {@code val}, clamped between {@code min} and {@code max}.
	 */
	public static double clamp(double val, double min, double max) {
		if (val < min) {
			val = min;
		} else if (val > max) {
			val = max;
		}
		return val;
	}

	/**
	 * A flooring modulus operator. Works similarly to the % operator, except
	 * this rounds towards negative infinity rather than towards 0.
	 * <p/>
	 * For example: <br/>
	 * -7 % 3 = -2; floorMod(-7, 3) = 2 <br/>
	 * -6 % 3 = -0; floorMod(-6, 3) = 0 <br/>
	 * -5 % 3 = -2; floorMod(-5, 3) = 1 <br/>
	 * -4 % 3 = -1; floorMod(-4, 3) = 2 <br/>
	 * -3 % 3 = -0; floorMod(-3, 3) = 0 <br/>
	 * -2 % 3 = -2; floorMod(-2, 3) = 1 <br/>
	 * -1 % 3 = -1; floorMod(-1, 3) = 2 <br/>
	 * 0 % 3 = 0; floorMod(0, 3) = 0 <br/>
	 * 1 % 3 = 1; floorMod(1, 3) = 1 <br/>
	 * 2 % 3 = 2; floorMod(2, 3) = 2 <br/>
	 * 3 % 3 = 0; floorMod(3, 3) = 0 <br/>
	 * 4 % 3 = 1; floorMod(4, 3) = 1 <br/>
	 * 
	 * @param num
	 *            The numerator of the modulus operator.
	 * @param den
	 *            The denominator of the modulus operator.
	 * @return num % den, rounded towards negative infinity.
	 */
	public static int floorMod(int num, int den) {
		if (den < 0) {
			throw new IllegalArgumentException(
					"floorMod does not currently support negative"
							+ "denominators");
		}
		if (num > 0) {
			return num % den;
		} else {
			int mod = (-num) % den;
			if (mod != 0) {
				mod = den - mod;
			}
			return mod;
		}
	}

	/**
	 * A flooring modulus operator. Works similarly to the % operator, except
	 * this rounds towards negative infinity rather than towards 0.
	 * <p/>
	 * For example: <br/>
	 * -7 % 3 = -2; floorMod(-7, 3) = 2 <br/>
	 * -6 % 3 = -0; floorMod(-6, 3) = 0 <br/>
	 * -5 % 3 = -2; floorMod(-5, 3) = 1 <br/>
	 * -4 % 3 = -1; floorMod(-4, 3) = 2 <br/>
	 * -3 % 3 = -0; floorMod(-3, 3) = 0 <br/>
	 * -2 % 3 = -2; floorMod(-2, 3) = 1 <br/>
	 * -1 % 3 = -1; floorMod(-1, 3) = 2 <br/>
	 * 0 % 3 = 0; floorMod(0, 3) = 0 <br/>
	 * 1 % 3 = 1; floorMod(1, 3) = 1 <br/>
	 * 2 % 3 = 2; floorMod(2, 3) = 2 <br/>
	 * 3 % 3 = 0; floorMod(3, 3) = 0 <br/>
	 * 4 % 3 = 1; floorMod(4, 3) = 1 <br/>
	 * 
	 * @param num
	 *            The numerator of the modulus operator.
	 * @param den
	 *            The denominator of the modulus operator.
	 * @return num % den, rounded towards negative infinity.
	 */
	public static double floorMod(double num, double den) {
		if (den < 0) {
			throw new IllegalArgumentException(
					"floorMod does not currently support negative"
							+ "denominators");
		}
		if (num > 0) {
			return num % den;
		} else {
			double mod = (-num) % den;
			if (mod != 0) {
				mod = den - mod;
			}
			return mod;
		}
	}

	/**
	 * Ensures that {@code index >= min} and {@code index <= max}.
	 * 
	 * @param index
	 *            The index of interest.
	 * @param min
	 *            The smallest valid value of index.
	 * @param max
	 *            The largest valid value of index.
	 * @throws IndexOutOfBoundsException
	 *             If index is not within the range of ({@code min}, {@code max}
	 *             ).
	 */
	public static void boundsAssert(int index, int min, int max) {
		if (index > max) {
			throw new IndexOutOfBoundsException(index + " is more than " + max);
		}
		if (index < min) {
			throw new IndexOutOfBoundsException(index + " is less than " + min);
		}
	}

	/**
	 * Word wraps a string. Inserts newline characters after some complete words
	 * to keep each line as close to {@code maxLength} as possible without
	 * exceeding it. No words are split in this manner. If a word is longer than
	 * {@code maxLength}, then the word is left intact and that line shall
	 * exceed maxLength.
	 * 
	 * @param str
	 *            The string to word wrap.
	 * @param maxLength
	 *            The maximum length one line of string can contain.
	 * @return A new String with word wrapping.
	 */
	public static String wrapString(String str, double maxLength) {
		StringTokenizer st = new StringTokenizer(str);
		double spaceLeft = maxLength;
		int spaceWidth = 1;
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			if ((word.length() + spaceWidth) > spaceLeft) {
				sb.append("\n" + word + " ");
				spaceLeft = maxLength - word.length();
			} else {
				sb.append(word + " ");
				spaceLeft -= (word.length() + spaceWidth);
			}
		}
		return sb.toString();
	}

	// public static int floorDiv(int num, int den) {
	// if(num > 0) {
	// return num / den;
	// } else {
	// int floor = -((-num)/den);
	// int mod = (-num) % den;
	// if(mod != 0) {
	// floor--;
	// }
	// return floor;
	// }
	// }
	//
}
