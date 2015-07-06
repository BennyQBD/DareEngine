/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.menu;

import java.util.Stack;

import engine.input.IButton;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.SpriteSheet;
import engine.util.Delay;

/**
 * Represents a complete menu system.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class MenuStack {
	private Stack<Menu> menuStack;
	private Menu defaultMenu;
	private SpriteSheet font;
	private IButton downKey;
	private IButton upKey;
	private IButton activateKey;
	private IButton toggleKey;
	private Delay moveDelay;
	private Delay toggleDelay;
	private Delay activateDelay;
	private Color fontColor;
	private Color selectionColor;
	private double offsetX;
	private double offsetY;
	private double scale;

	/**
	 * Creates a new MenuStack.
	 * 
	 * @param font
	 *            The font to render text with.
	 * @param fontColor
	 *            The color to use for normal menu options.
	 * @param selectionColor
	 *            The color to use for the currently selected menu option.
	 * @param offsetX
	 *            The location, on X, to begin rendering at.
	 * @param offsetY
	 *            The location, on Y, to begin rendering at.
	 * @param scale
	 *            How large each character of text should be.
	 * @param upKey
	 *            The button that moves the menu up.
	 * @param downKey
	 *            The button that moves the menu down.
	 * @param activateKey
	 *            The button that activates the menu.
	 * @param toggleKey
	 *            The button that can either bring the menu up, or go back a
	 *            level.
	 * @param usageDelayLength
	 *            The minimum time between doing something with the menu.
	 * @param defaultMenu
	 *            The initial level of the Menu.
	 */
	public MenuStack(SpriteSheet font, Color fontColor, Color selectionColor,
			double offsetX, double offsetY, double scale, IButton upKey,
			IButton downKey, IButton activateKey, IButton toggleKey,
			double usageDelayLength, Menu defaultMenu) {
		this.font = font;
		this.menuStack = new Stack<Menu>();
		this.toggleKey = toggleKey;
		this.toggleDelay = new Delay(usageDelayLength);
		this.defaultMenu = defaultMenu;
		this.fontColor = fontColor;
		this.selectionColor = selectionColor;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scale = scale;
		this.upKey = upKey;
		this.downKey = downKey;
		this.activateKey = activateKey;
		this.moveDelay = new Delay(usageDelayLength);
		this.activateDelay = new Delay(usageDelayLength);
	}

	/**
	 * Closes out of the menu entirely.
	 */
	public void close() {
		while (isShowing()) {
			pop();
		}
	}

	/**
	 * If a menu is open, go back a level. If no menu is open, open the default
	 * menu.
	 */
	public void toggleVisibility() {
		if (isShowing()) {
			pop();
		} else {
			push(defaultMenu);
		}
	}

	/**
	 * Updates the MenuStack.
	 * 
	 * @param delta
	 *            Amount of time passed since last update.
	 */
	public void update(double delta) {
		if (toggleDelay.over(delta) && toggleKey.isDown()) {
			toggleDelay.reset();
			toggleVisibility();
		}

		if (!isShowing()) {
			return;
		}

		boolean canMove = moveDelay.over(delta);
		if (canMove && upKey.isDown()) {
			getCurrentMenu().move(-1);
			moveDelay.reset();
		}
		if (canMove && downKey.isDown()) {
			getCurrentMenu().move(1);
			moveDelay.reset();
		}

		if (activateDelay.over(delta) && activateKey.isDown()) {
			getCurrentMenu().activate();
			activateDelay.reset();
		}
	}

	/**
	 * Goes back a menu level.
	 */
	public void pop() {
		Menu menu = getCurrentMenu();
		if (menu != null) {
			menu.setMenuStack(null);
			menuStack.pop();
		}
	}

	/**
	 * Add a new menu level.
	 * 
	 * @param menu
	 *            The new menu to add.
	 */
	public void push(Menu menu) {
		menu.setMenuStack(this);
		menuStack.push(menu);
	}

	/**
	 * Render the current menu.
	 * 
	 * @param target
	 *            The render context to draw the menu with.
	 */
	public void render(IRenderContext target) {
		if (isShowing()) {
			getCurrentMenu().render(target, font, offsetX, offsetY, scale,
					selectionColor, fontColor);
		}
	}

	private Menu getCurrentMenu() {
		if (menuStack.empty()) {
			return null;
		}
		return menuStack.peek();
	}

	/**
	 * Whether or not a menu is currently open.
	 * @return True if a menu is open, false other wise.
	 */
	public boolean isShowing() {
		return !menuStack.empty();
	}
}
