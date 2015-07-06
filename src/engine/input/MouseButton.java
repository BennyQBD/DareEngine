/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Handles buttons on a mouse
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class MouseButton extends BaseButton {
	/**
	 * Creates a new MouseButton
	 * 
	 * @param input
	 *            The input system
	 * @param mouseButton
	 *            The code for the mouse button being checked. Should be one of
	 *            the IInput.MOUSE_BUTTON values.
	 */
	public MouseButton(IInput input, int mouseButton) {
		this(input, new int[] { mouseButton });
	}

	/**
	 * Creates a new MouseButton
	 * 
	 * @param input
	 *            The input system
	 * @param mouseButtons
	 *            The codes for the mouse buttons being checked. They should be
	 *            IInput.MOUSE_BUTTON values.
	 */
	public MouseButton(IInput input, int[] mouseButtons) {
		super(input, mouseButtons, new BaseButton.Command() {
			@Override
			public boolean isDown(IInput input, int code) {
				return input.getMouse(code);
			}
		});
	}
}
