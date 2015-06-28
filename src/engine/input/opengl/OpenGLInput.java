package engine.input.opengl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import engine.input.IInput;

public class OpenGLInput implements IInput {
	@Override
	public boolean getMouse(int button) {
		return Mouse.isButtonDown(button);
	}

	@Override
	public int getMouseX() {
		return Mouse.getX();
	}

	@Override
	public int getMouseY() {
		return Mouse.getY();
	}

	@Override
	public boolean getKey(int code) {
		return Keyboard.isKeyDown(code);
	}

}
