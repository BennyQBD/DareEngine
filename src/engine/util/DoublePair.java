/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util;

/**
 * Stores two doubles.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class DoublePair {
	/**
	 * Creates a new DoublePair
	 * 
	 * @param val1
	 *            The first double in the pair.
	 * @param val2
	 *            The second double in the pair.
	 */
	public DoublePair(double val1, double val2) {
		this.setVal1(val1);
		this.setVal2(val2);
	}

	private double val1;
	private double val2;

	/**
	 * Gets the first double.
	 * 
	 * @return The first double.
	 */
	public double getVal1() {
		return val1;
	}

	/**
	 * Gets the second double.
	 * 
	 * @return The second double.
	 */
	public double getVal2() {
		return val2;
	}

	/**
	 * Sets the first double.
	 * 
	 * @param val1
	 *            New value of the first double.
	 */
	public void setVal1(double val1) {
		this.val1 = val1;
	}

	/**
	 * Sets the second double.
	 * 
	 * @param val2
	 *            New value of the second double.
	 */
	public void setVal2(double val2) {
		this.val2 = val2;
	}
}
