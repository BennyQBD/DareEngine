/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util;

/**
 * Various utility functions for Debugging.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Debug {
	private static boolean ignoringFrameCap = false;
	private static boolean logToConsole = false;

	/**
	 * Initializes the Debugging system
	 * 
	 * @param ignoringFrameCap
	 *            Whether the frame cap is being ignored or not.
	 * @param logToConsole
	 *            Whether or not to log to console.
	 */
	public static void init(boolean ignoringFrameCap, boolean logToConsole) {
		Debug.ignoringFrameCap = ignoringFrameCap;
		Debug.logToConsole = logToConsole;
	}

	/**
	 * Logs something in a certain location.
	 * 
	 * @param str
	 *            The object to log.
	 */
	public static void log(Object str) {
		if (logToConsole) {
			System.out.println(str.toString());
		}
	}

	/**
	 * @return the ignoringFrameCap
	 */
	public static boolean isIgnoringFrameCap() {
		return ignoringFrameCap;
	}
}
