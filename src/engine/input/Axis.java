package engine.input;

import engine.util.Util;

public class Axis {
	private IInput input;
	private int[] negativeKeys;
	private int[] positiveKeys;
	private int[] negativeMouseButtons;
	private int[] positiveMouseButtons;
	private int[] negativeJoystickButtons;
	private int[] positiveJoystickButtons;
	private int[] joystickAxes;
	private double mouseAmtX;
	private double mouseAmtY;
	private int joystick;

	public Axis(IInput input, int joystick, int[] negativeJoystickButtons,
			int[] positiveJoystickButtons, int[] joystickAxes,
			double mouseAmtX, double mouseAmtY) {
		this(input, null, null, null, null, mouseAmtX, mouseAmtY, joystick, negativeJoystickButtons,
				positiveJoystickButtons, joystickAxes);
	}

	public Axis(IInput input, int[] negativeKeys, int[] positiveKeys,
			int[] negativeMouseButtons, int[] positiveMouseButtons) {
		this(input, negativeKeys, positiveKeys, negativeMouseButtons,
				positiveMouseButtons, 0.0, 0.0, -1, null, null, null);
	}

	public Axis(IInput input, int[] negativeKeys, int[] positiveKeys,
			int[] negativeMouseButtons, int[] positiveMouseButtons,
			double mouseAmtX, double mouseAmtY, int joystick,
			int[] negativeJoystickButtons, int[] positiveJoystickButtons,
			int[] joystickAxes) {
		this.input = input;
		this.joystick = joystick;
		this.negativeKeys = negativeKeys;
		this.positiveKeys = positiveKeys;
		this.negativeMouseButtons = negativeMouseButtons;
		this.positiveMouseButtons = positiveMouseButtons;
		this.negativeJoystickButtons = negativeJoystickButtons;
		this.positiveJoystickButtons = positiveJoystickButtons;
		this.mouseAmtX = mouseAmtX;
		this.mouseAmtY = mouseAmtY;
		this.joystickAxes = joystickAxes;
	}

	private double getKey(int[] keys) {
		if (keys == null) {
			return 0.0;
		}
		double result = 0.0;
		for (int i = 0; i < keys.length; i++) {
			if (input.getKey(keys[i])) {
				result += 1.0;
			}
		}
		return result;
	}

	private double getMouseButton(int[] mouseButtons) {
		if (mouseButtons == null) {
			return 0.0;
		}
		double result = 0.0;
		for (int i = 0; i < mouseButtons.length; i++) {
			if (input.getMouse(mouseButtons[i])) {
				result += 1.0;
			}
		}
		return result;
	}

	private double getJoystickButton(int[] joystickButtons) {
		if (joystickButtons == null || joystick == -1) {
			return 0.0;
		}
		double result = 0.0;
		for (int i = 0; i < joystickButtons.length; i++) {
			if (input.getJoystickButton(joystick, joystickButtons[i])) {
				result += 1.0;
			}
		}
		return result;
	}

	private double getJoystickAxes(int[] joystickAxes) {
		if (joystickAxes == null || joystick == -1) {
			return 0.0;
		}
		double result = 0.0;
		for (int i = 0; i < joystickAxes.length; i++) {
			result += input.getJoystickAxis(joystick, joystickAxes[i]);
		}
		return result;
	}

	private double getJoystickAmount() {
		if (joystick == -1) {
			return 0.0;
		}

		return (getJoystickButton(positiveJoystickButtons) - getJoystickButton(negativeJoystickButtons))
				+ getJoystickAxes(joystickAxes);
	}
	
	private double getMouseAmt() {
		return input.getMouseDeltaX() * mouseAmtX + input.getMouseDeltaY() * mouseAmtY;
	}

	public double getAmount() {
		double result = (getKey(positiveKeys) - getKey(negativeKeys))
				+ (getMouseButton(positiveMouseButtons) - getMouseButton(negativeMouseButtons))
				+ getJoystickAmount() + getMouseAmt();
		return Util.clamp(result, -1.0, 1.0);
	}
}
