package engine.input;

public class KeyButton extends BaseButton {
	public KeyButton(IInput input, int keyCode) {
		this(input, new int[] { keyCode });
	}

	public KeyButton(IInput input, int[] keyCodes) {
		super(input, keyCodes, new BaseButton.Command() {
			@Override
			public boolean isDown(IInput input, int code) {
				return input.getKey(code);
			}
		});
	}
}
