/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering.opengl;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import engine.audio.IAudioDevice;
import engine.audio.openal.OpenALAudioDevice;
import engine.input.IInput;
import engine.input.opengl.OpenGLInput;
import engine.rendering.IDisplay;
import engine.rendering.IRenderContext;
import engine.rendering.IRenderDevice;
import engine.rendering.RenderContext;
import engine.rendering.RenderTarget;
import engine.util.Debug;

/**
 * A display that is capable of recieving OpenGL rendering.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class OpenGLDisplay implements IDisplay {
	private final IRenderDevice device;
	private final IAudioDevice audioDevice;
	private final IRenderContext frameBuffer;
	private final IInput input;

	private GLFWErrorCallback errorCallback;
	private long window;

	private RenderTarget target;

	/**
	 * Creates a new OpenGL display.
	 * 
	 * @param width The width of the display, in pixels.
	 * @param height The height of the display, in pixels.
	 * @param title The text appearing in the display's title bar.
	 */
	public OpenGLDisplay(int width, int height, String title) {
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		if (glfwInit() != GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2,
				(GLFWvidmode.height(vidmode) - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(Debug.isIgnoringFrameCap() ? 0 : 1);

		glfwShowWindow(window);
		GLContext.createFromCurrent();

		device = new OpenGLRenderDevice(width, height);
		this.target = new RenderTarget(device, width, height, 0, 0);
		frameBuffer = new RenderContext(device, target);
		input = new OpenGLInput(window);
		audioDevice = new OpenALAudioDevice();
	}

	@Override
	public void present() {
		glfwSwapBuffers(window);
	}

	@Override
	public void update() {
		glfwPollEvents();
		input.update();
	}

	@Override
	public boolean isClosed() {
		return (glfwWindowShouldClose(window) == GL_TRUE) ? true : false;
	}

	@Override
	public void dispose() {
		device.dispose();
		target.dispose();
		audioDevice.dispose();
		frameBuffer.dispose();
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
	}

	@Override
	public IRenderContext getRenderContext() {
		return frameBuffer;
	}

	@Override
	public IRenderDevice getRenderDevice() {
		return device;
	}

	@Override
	public IInput getInput() {
		return input;
	}

	@Override
	public IAudioDevice getAudioDevice() {
		return audioDevice;
	}
}
