/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Handles multiple buttons at once
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class CompoundButton implements IButton {
	private IButton[] buttons;

	/**
	 * Creates a new CompoundButton
	 * 
	 * @param button1 The first button being checked.
	 * @param button2 The second button being checked.
	 */
	public CompoundButton(IButton button1, IButton button2) {
		this(new IButton[] { button1, button2 });
	}

	/**
	 * Creates a new CompoundButton
	 * 
	 * @param buttons The list of buttons being checked.
	 */
	public CompoundButton(IButton[] buttons) {
		this.buttons = buttons;
	}

	@Override
	public boolean isDown() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isDown()) {
				return true;
			}
		}
		return false;
	}
}
