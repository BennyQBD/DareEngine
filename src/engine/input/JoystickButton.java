package engine.input;

public class JoystickButton extends BaseButton {
	public JoystickButton(IInput input, int joystick, int joystickButton) {
		this(input, joystick, new int[] { joystickButton });
	}

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
