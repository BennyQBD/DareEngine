/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Handles buttons on a joystick.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class JoystickButton extends BaseButton {
	/**
	 * Creates a new JoystickButton
	 * 
	 * @param input
	 *            The input system
	 * @param joystick
	 *            The joystick. Should be one of the IInput.JOYSTICK values.
	 * @param joystickButton
	 *            The button on the joystick being checked.
	 */
	public JoystickButton(IInput input, int joystick, int joystickButton) {
		this(input, joystick, new int[] { joystickButton });
	}

	/**
	 * Creates a new JoystickButton
	 * 
	 * @param input
	 *            The input system
	 * @param joystick
	 *            The joystick. Should be one of the IInput.JOYSTICK values.
	 * @param joystickButtons
	 *            The buttons on the joystick being checked.
	 */
	public JoystickButton(IInput input, final int joystick,
			int[] joystickButtons) {
		super(input, joystickButtons, new BaseButton.Command() {
			@Override
			public boolean isDown(IInput input, int code) {
				return input.getJoystickButton(joystick, code);
			}
		});
	}
}
