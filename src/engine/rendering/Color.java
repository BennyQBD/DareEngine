package engine.rendering;

import engine.util.Util;

public class Color {
	public static final Color WHITE = new Color(1.0, 1.0, 1.0);

	private static final int ARGB_COMPONENT_BITS = 8;
	private static final int ARGB_COMPONENT_MASK = (1 << ARGB_COMPONENT_BITS) - 1;
	private static final int ARGB_NUM_COMPONENTS = 4;

	private double red;
	private double green;
	private double blue;

	public Color(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public static int makeARGB(int a, int r, int g, int b) {
		return ((a & ARGB_COMPONENT_MASK) << getComponentShift(0))
				| ((r & ARGB_COMPONENT_MASK) << getComponentShift(1))
				| ((g & ARGB_COMPONENT_MASK) << getComponentShift(2))
				| (b & ARGB_COMPONENT_MASK) << getComponentShift(3);
	}

	public static int makeARGB(int r, int g, int b) {
		return makeARGB(ARGB_COMPONENT_MASK, r, g, b);
	}

	public static int makeARGB(double a, double r, double g, double b) {
		return makeARGB(doubleToComponent(a), doubleToComponent(r),
				doubleToComponent(g), doubleToComponent(b));
	}

	public static int makeARGB(double r, double g, double b) {
		return makeARGB(1.0, r, g, b);
	}

	private static int doubleToComponent(double c) {
		return (int) (Util.saturate(c) * ARGB_COMPONENT_MASK + 0.5);
	}

	private static int getComponentShift(int component) {
		return (ARGB_COMPONENT_BITS * (ARGB_NUM_COMPONENTS - component - 1));
	}

	public int scaleToARGB(double scaleAmt) {
		return makeARGB(red * scaleAmt, green * scaleAmt, blue * scaleAmt);
	}

	public double getRed() {
		return red;
	}

	public double getGreen() {
		return green;
	}

	public double getBlue() {
		return blue;
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
