/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Handles buttons from a keyboard
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class KeyButton extends BaseButton {
	/**
	 * Creates a new KeyButton
	 * 
	 * @param input
	 *            The input system
	 * @param keyCode
	 *            The code for the key being checked. Should be one of the
	 *            IInput.KEY values.
	 */
	public KeyButton(IInput input, int keyCode) {
		this(input, new int[] { keyCode });
	}

	/**
	 * Creates a new KeyButton
	 * 
	 * @param input
	 *            The input system
	 * @param keyCodes
	 *            The codes for the keys being checked. They should be
	 *            IInput.KEY values.
	 */
	public KeyButton(IInput input, int[] keyCodes) {
		super(input, keyCodes, new BaseButton.Command() {
			@Override
			public boolean isDown(IInput input, int code) {
				return input.getKey(code);
			}
		});
	}
}
