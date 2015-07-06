/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Interface for an axis based input device
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IAxis {
	/**
	 * Gets the current value along the axis. -1 is smallest input, 1 is largest
	 * input.
	 * 
	 * @return The current value of the axis in the range (-1, 1).
	 */
	public double getAmount();
}
