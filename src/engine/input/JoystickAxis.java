package engine.input;

import engine.util.Util;

public class JoystickAxis implements IAxis {
	private IInput input;
	private int joystick;
	private int[] joystickAxes;

	public JoystickAxis(IInput input, int joystick, int joystickAxis) {
		this(input, joystick, new int[] { joystickAxis });
	}

	public JoystickAxis(IInput input, int joystick, int[] joystickAxes) {
		this.input = input;
		this.joystick = joystick;
		this.joystickAxes = joystickAxes;
	}

	@Override
	public double getAmount() {
		if (joystickAxes == null || joystick == -1) {
			return 0.0;
		}
		double result = 0.0;
		for (int i = 0; i < joystickAxes.length; i++) {
			result += input.getJoystickAxis(joystick, joystickAxes[i]);
		}
		return Util.clamp(result, -1.0, 1.0);
	}
}
