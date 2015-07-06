/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Base class for typical buttons
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class BaseButton implements IButton {
	/**
	 * Decides whether a certain code is down or not.
	 */
	public static interface Command {
		/**
		 * Decides whether a certain code is down or not.
		 * 
		 * @param input
		 *            The input system
		 * @param code
		 *            The button code
		 * @return True if the button specified by the code is down in the input
		 *         system, false otherwise.
		 */
		public boolean isDown(IInput input, int code);
	}

	private IInput input;
	private int[] codes;
	private Command command;

	/**
	 * Creates a new BaseButton.
	 * 
	 * @param input
	 *            The input system
	 * @param codes
	 *            The list of codes this button is checking
	 * @param command
	 *            The method of deciding if a code is down or not in the input
	 *            system.
	 */
	public BaseButton(IInput input, int[] codes, Command command) {
		this.input = input;
		this.codes = codes;
		this.command = command;
	}

	@Override
	public boolean isDown() {
		if (codes == null) {
			return false;
		}
		for (int i = 0; i < codes.length; i++) {
			if (command.isDown(input, codes[i])) {
				return true;
			}
		}
		return false;
	}
}
