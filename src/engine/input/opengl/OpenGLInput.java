/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input.opengl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.input.IInput;

/**
 * An implementation of IInput compatible with {@link engine.rendering.opengl.OpenGLDisplay}.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class OpenGLInput implements IInput {
	private long inputSource;
	private DoubleBuffer mouseX;
	private DoubleBuffer mouseY;
	private FloatBuffer[] joystickAxes;
	private ByteBuffer[] joystickButtons;
	private double mouseDeltaX;
	private double mouseDeltaY;
	private boolean hasBeenUpdated;

	public OpenGLInput(long inputSource) {
		this.inputSource = inputSource;
		this.mouseX = BufferUtils.createDoubleBuffer(1);
		this.mouseY = BufferUtils.createDoubleBuffer(1);
		this.joystickAxes = new FloatBuffer[IInput.JOYSTICK_LAST];
		this.joystickButtons = new ByteBuffer[joystickAxes.length];
		this.mouseDeltaX = 0;
		this.mouseDeltaY = 0;
		this.hasBeenUpdated = false;
	}

	@Override
	public boolean getMouse(int button) {
		return glfwGetMouseButton(inputSource, button) == GL_TRUE ? true
				: false;
	}

	private void initJoystick(int i) {
		if (joystickAxes[i] == null) {
			updateJoystick(i);
		}
	}

	private void updateJoystick(int i) {
		FloatBuffer newAxes = glfwGetJoystickAxes(i);
		ByteBuffer newButtons = glfwGetJoystickButtons(i);

		if (newAxes != null) {
			joystickAxes[i] = newAxes;
		} else if (joystickAxes[i] == null) {
			joystickAxes[i] = createDefaultJoystickAxes();
		}

		if (newButtons != null) {
			joystickButtons[i] = newButtons;
		} else if (joystickButtons[i] == null) {
			joystickButtons[i] = createDefaultJoystickButtons();
		}
	}

	private ByteBuffer createDefaultJoystickButtons() {
		return BufferUtils.createByteBuffer(0);
	}

	private FloatBuffer createDefaultJoystickAxes() {
		return BufferUtils.createFloatBuffer(0);
	}

	private void updateJoysticks() {
		for (int i = 0; i < joystickAxes.length; i++) {
			if (joystickAxes[i] == null) {
				continue;
			}

			updateJoystick(i);
		}
	}

	private void updateMouse() {
		double mouseXBefore = getMouseX();
		double mouseYBefore = getMouseY();
		glfwGetCursorPos(inputSource, mouseX, mouseY);

		if (hasBeenUpdated) {
			mouseDeltaX = getMouseX() - mouseXBefore;
			mouseDeltaY = getMouseY() - mouseYBefore;
		}
		hasBeenUpdated = true;
	}

	@Override
	public void update() {
		updateMouse();
		updateJoysticks();
	}

	@Override
	public boolean getKey(int code) {
		return glfwGetKey(inputSource, code) == GL_TRUE ? true : false;
	}

	@Override
	public double getMouseX() {
		return mouseX.get(0);
	}

	@Override
	public double getMouseY() {
		return mouseY.get(0);
	}

	@Override
	public double getMouseDeltaX() {
		return mouseDeltaX;
	}

	@Override
	public double getMouseDeltaY() {
		return mouseDeltaY;
	}

	@Override
	public String getJoystickName(int joystick) {
		return glfwGetJoystickName(joystick);
	}

	@Override
	public int getNumJoystickAxes(int joystick) {
		initJoystick(joystick);
		return joystickAxes[joystick].capacity();
	}

	@Override
	public double getJoystickAxis(int joystick, int axis) {
		int numAxes = getNumJoystickAxes(joystick);
		if (axis < 0 || axis >= numAxes) {
			return 0.0;
		}
		return (double) joystickAxes[joystick].get(axis);
	}

	@Override
	public int getNumJoystickButtons(int joystick) {
		initJoystick(joystick);
		return joystickButtons[joystick].capacity();
	}

	@Override
	public boolean getJoystickButton(int joystick, int button) {
		int numButtons = getNumJoystickButtons(joystick);
		if (button < 0 || button >= numButtons) {
			return false;
		}
		return joystickButtons[joystick].get(button) == 1 ? true : false;
	}
}
