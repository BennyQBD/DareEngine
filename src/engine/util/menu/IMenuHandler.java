/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.menu;

/**
 * Interface for handling a selected menu option.
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IMenuHandler {
	/**
	 * Called when a particular menu option is selected.
	 * 
	 * @param option The option that was selected.
	 * @param stack The MenuStack that the menu is a part of.
	 */
	public void handleMenu(int option, MenuStack stack);
}
