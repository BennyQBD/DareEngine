/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.menu;

import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.SpriteSheet;
import engine.util.Util;

/**
 * Represents one level of a menu system.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Menu {
	private String[] options;
	private IMenuHandler handler;
	private int selectionIndex;

	private MenuStack menuStack;

	/**
	 * Creates a new Menu.
	 * 
	 * @param options
	 *            All the options in the menu.
	 * @param handler
	 *            The handler that processes the options.
	 */
	public Menu(String[] options, IMenuHandler handler) {
		this.options = options;
		this.handler = handler;
		this.selectionIndex = 0;
		this.menuStack = null;
	}

	/**
	 * Sets the MenuStack for this menu.
	 * 
	 * @param stack
	 *            The MenuStack to set.
	 */
	public void setMenuStack(MenuStack stack) {
		this.menuStack = stack;
	}

	/**
	 * Activates the menu handler for the currently selected option.
	 */
	public void activate() {
		handler.handleMenu(selectionIndex, menuStack);
	}

	/**
	 * Moves the menu up or down by the specified amount.
	 * 
	 * @param amt
	 *            How much to move up or down by.
	 */
	public void move(int amt) {
		selectionIndex = Util.floorMod(selectionIndex + amt, options.length);
	}

	/**
	 * Renders the menu.
	 * 
	 * @param target
	 *            The render context to draw the menu with.
	 * @param font
	 *            The font to render text with.
	 * @param offsetX
	 *            The location, on X, to begin rendering at.
	 * @param offsetY
	 *            The location, on Y, to begin rendering at.
	 * @param scale
	 *            How large each character of text should be.
	 * @param selectionColor
	 *            The color to use for the currently selected menu option.
	 * @param fontColor
	 *            The color to use for normal menu options.
	 */
	public void render(IRenderContext target, SpriteSheet font, double offsetX,
			double offsetY, double scale, Color selectionColor, Color fontColor) {
		double y = offsetY;
		for (int i = 0; i < options.length; i++) {
			Color color = i == selectionIndex ? selectionColor : fontColor;
			y = target.drawString(options[i], font, offsetX, y, scale, color,
					1.0);
		}
	}
}
