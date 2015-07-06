package engine.input;

public class MouseButton extends BaseButton {
	public MouseButton(IInput input, int mouseButton) {
		this(input, new int[] { mouseButton });
	}

	public MouseButton(IInput input, int[] mouseButtons) {
		super(input, mouseButtons, new BaseButton.Command() {
			@Override
			public boolean isDown(IInput input, int code) {
				return input.getMouse(code);
			}
		});
	}
}
