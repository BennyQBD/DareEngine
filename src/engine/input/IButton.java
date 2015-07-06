/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Interface for a binary input device.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IButton {
	/**
	 * Returns whether this button is currently pressed
	 * 
	 * @return True if the button is pressed, false otherwise.
	 */
	public boolean isDown();
}
