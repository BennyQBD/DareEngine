package engine.input;

import java.util.ArrayList;
import java.util.List;

import engine.util.Util;
import engine.util.parsing.Config;

public class Control {
	private int[] keyCodes;
	private IInput input;

	public Control(IInput input, Config config, String controlName) {
		this.input = input;
		List<Integer> codes = new ArrayList<Integer>();
		int currentControl = 0;
		String codeStr;
		while ((codeStr = config.getString(controlName + (currentControl++))) != null) {
			codes.add(Integer.parseInt(codeStr));
		}
		this.keyCodes = Util.toInt(codes.toArray(new Integer[0]));
	}

	public boolean isDown() {
		for (int i = 0; i < keyCodes.length; i++) {
			if (input.getKey(keyCodes[i])) {
				return true;
			}
		}
		return false;
	}
}
