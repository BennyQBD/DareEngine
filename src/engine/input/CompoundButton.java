package engine.input;

public class CompoundButton implements IButton {
	private IButton[] buttons;

	public CompoundButton(IButton button1, IButton button2) {
		this(new IButton[] { button1, button2 });
	}

	public CompoundButton(IButton[] buttons) {
		this.buttons = buttons;
	}

	@Override
	public boolean isDown() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isDown()) {
				return true;
			}
		}
		return false;
	}
}
