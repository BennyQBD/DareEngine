/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util;

/**
 * Generates unique IDs. This should only be used to initialize constants, and
 * should not be used in running code.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class IDAssigner {
	private static int currentId = 0;

	/**
	 * Returns a new integer with each call.
	 * 
	 * @return A new, unique integer ID.
	 */
	public static int getId() {
		return currentId++;
	}
}
