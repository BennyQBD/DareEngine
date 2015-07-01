package engine.input;

public class Button {
	private int[] keyCodes;
	private int[] mouseButtons;
	private int[] joystickButtons;
	private int joystick;
	private IInput input;

	public Button(IInput input, int[] keyCodes, int[] mouseButtons) {
		this(input, keyCodes, mouseButtons, -1, null);
	}
	
	public Button(IInput input, int joystick, int[] joystickButtons) {
		this(input, null, null, joystick, joystickButtons);
	}
	
	public Button(IInput input, int[] keyCodes, int[] mouseButtons,
			int joystick, int[] joystickButtons) {
		this.input = input;
		this.keyCodes = keyCodes;
		this.mouseButtons = mouseButtons;
		this.joystick = joystick;
		this.joystickButtons = joystickButtons;
	}
	
	private boolean getKeyDown() {
		if(keyCodes == null) {
			return false;
		}
		for (int i = 0; i < keyCodes.length; i++) {
			if (input.getKey(keyCodes[i])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean getMouseDown() {
		if(mouseButtons == null) {
			return false;
		}
		for (int i = 0; i < mouseButtons.length; i++) {
			if (input.getMouse(mouseButtons[i])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean getJoystickButtonDown() {
		if(joystickButtons == null || joystick == -1) {
			return false;
		}
		for (int i = 0; i < joystickButtons.length; i++) {
			if (input.getJoystickButton(joystick, joystickButtons[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDown() {
		return getKeyDown() || getMouseDown() || getJoystickButtonDown();
	}
}
