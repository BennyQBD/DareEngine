/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import engine.util.Util;

/**
 * Represents a Color.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Color {
	/** Pure White. */
	public static final Color WHITE = new Color(1.0, 1.0, 1.0);
	/** Pure Black. */
	public static final Color BLACK = new Color(0.0, 0.0, 0.0);

	private static final int ARGB_COMPONENT_BITS = 8;
	private static final int ARGB_COMPONENT_MASK = (1 << ARGB_COMPONENT_BITS) - 1;
	private static final int ARGB_NUM_COMPONENTS = 4;

	private double red;
	private double green;
	private double blue;
	private double alpha;

	/**
	 * Creates a new Color.
	 * 
	 * @param red
	 *            The amount of red, in the range of (0, 1).
	 * @param green
	 *            The amount of green, in the range of (0, 1).
	 * @param blue
	 *            The amount of blue, in the range of (0, 1).
	 */
	public Color(double red, double green, double blue) {
		this(red, green, blue, 1.0);
	}

	/**
	 * Creates a new Color.
	 * 
	 * @param red
	 *            The amount of red, in the range of (0, 1).
	 * @param green
	 *            The amount of green, in the range of (0, 1).
	 * @param blue
	 *            The amount of blue, in the range of (0, 1).
	 * @param alpha
	 *            The amount of transparency, in the range of (0, 1). 0 is fully
	 *            transparent, and 1 is fully opaque.
	 */
	public Color(double red, double green, double blue, double alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	/**
	 * Creates a new Color with a shade a grey.
	 * 
	 * @param amt
	 *            The amount of grey, in the range of (0, 1).
	 */
	public Color(double amt) {
		this(amt, amt, amt);
	}

	/**
	 * Creates a 32-bit ARGB color.
	 * 
	 * @param a
	 *            The amount of alpha, in range of (0, 255).
	 * @param r
	 *            The amount of red, in range of (0, 255).
	 * @param g
	 *            The amount of green, in range of (0, 255).
	 * @param b
	 *            The amount of blue, in range of (0, 255).
	 * @return A 32-bit ARGB color.
	 */
	public static int makeARGB(int a, int r, int g, int b) {
		return ((a & ARGB_COMPONENT_MASK) << getComponentShift(0))
				| ((r & ARGB_COMPONENT_MASK) << getComponentShift(1))
				| ((g & ARGB_COMPONENT_MASK) << getComponentShift(2))
				| (b & ARGB_COMPONENT_MASK) << getComponentShift(3);
	}

	/**
	 * Creates a 32-bit ARGB color.
	 * 
	 * @param r
	 *            The amount of red, in range of (0, 255).
	 * @param g
	 *            The amount of green, in range of (0, 255).
	 * @param b
	 *            The amount of blue, in range of (0, 255).
	 * @return A 32-bit ARGB color.
	 */
	public static int makeARGB(int r, int g, int b) {
		return makeARGB(ARGB_COMPONENT_MASK, r, g, b);
	}

	/**
	 * Creates a 32-bit ARGB color.
	 * 
	 * @param a
	 *            The amount of alpha, in range of (0, 1).
	 * @param r
	 *            The amount of red, in range of (0, 1).
	 * @param g
	 *            The amount of green, in range of (0, 1).
	 * @param b
	 *            The amount of blue, in range of (0, 1).
	 * @return A 32-bit ARGB color.
	 */
	public static int makeARGB(double a, double r, double g, double b) {
		return makeARGB(doubleToComponent(a), doubleToComponent(r),
				doubleToComponent(g), doubleToComponent(b));
	}

	/**
	 * Creates a 32-bit ARGB color.
	 * 
	 * @param r
	 *            The amount of red, in range of (0, 1).
	 * @param g
	 *            The amount of green, in range of (0, 1).
	 * @param b
	 *            The amount of blue, in range of (0, 1).
	 * @return A 32-bit ARGB color.
	 */
	public static int makeARGB(double r, double g, double b) {
		return makeARGB(1.0, r, g, b);
	}

	private static int doubleToComponent(double c) {
		return (int) (Util.saturate(c) * ARGB_COMPONENT_MASK + 0.5);
	}

	/**
	 * Gets a component of an ARGB color.
	 * 
	 * @param pixel
	 *            A 32-bit ARGB color.
	 * @param component
	 *            Which component to get, in range of (0, 3). <br/>
	 *            0 -> Alpha <br/>
	 *            1 -> Red <br/>
	 *            2 -> Green <br/>
	 *            3 -> Blue
	 * @return The component of the ARGB Color.
	 */
	public static byte getARGBComponent(int pixel, int component) {
		return (byte) ((pixel >> getComponentShift(component)) & ARGB_COMPONENT_MASK);
	}

	private static int getComponentShift(int component) {
		return (ARGB_COMPONENT_BITS * (ARGB_NUM_COMPONENTS - component - 1));
	}

	/**
	 * Returns a 32-Bit ARGB version of this color, with intensity scaled by
	 * scaleAmt.
	 * 
	 * @param scaleAmt
	 *            The amount to scale the intensity of this color during
	 *            conversion.
	 * @return A 32-Bit ARGB version of this color, with intensity scaled by
	 *         scaleAmt.
	 */
	public int scaleToARGB(double scaleAmt) {
		return makeARGB(red * scaleAmt, green * scaleAmt, blue * scaleAmt);
	}

	/**
	 * Get the amount of red in this color.
	 * 
	 * @return The amount of green in this color.
	 */
	public double getRed() {
		return red;
	}

	/**
	 * Get the amount of green in this color.
	 * 
	 * @return The amount of green in this color.
	 */
	public double getGreen() {
		return green;
	}

	/**
	 * Get the amount of blue in this color.
	 * 
	 * @return The amount of blue in this color.
	 */
	public double getBlue() {
		return blue;
	}

	/**
	 * Get the amount of alpha in this color.
	 * 
	 * @return The amount of alpha in this color.
	 */
	public double getAlpha() {
		return alpha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(blue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(green);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(red);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (Double.doubleToLongBits(blue) != Double
				.doubleToLongBits(other.blue))
			return false;
		if (Double.doubleToLongBits(green) != Double
				.doubleToLongBits(other.green))
			return false;
		if (Double.doubleToLongBits(red) != Double.doubleToLongBits(other.red))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Color [red=" + red + ", green=" + green + ", blue=" + blue
				+ "]";
	}
}
