package engine.input;

public class BaseButton implements IButton {
	public static interface Command {
		public boolean isDown(IInput input, int code);
	}
	private IInput input;
	private int[] codes;
	private Command command;
	
	public BaseButton(IInput input, int[] codes, Command command) {
		this.input = input;
		this.codes = codes;
		this.command = command;
	}
	
	public boolean isDown() {
		if(codes == null) {
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
