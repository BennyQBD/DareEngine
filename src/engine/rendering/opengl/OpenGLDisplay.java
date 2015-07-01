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
import engine.util.Debug;

public class OpenGLDisplay implements IDisplay {
	private final IRenderDevice device;
	private final IAudioDevice audioDevice;
	private final IRenderContext frameBuffer;
	private final IInput input;

	private GLFWErrorCallback errorCallback;
	private long window;

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
		glfwSwapInterval(Debug.IGNORE_FRAME_CAP ? 0 : 1);

		glfwShowWindow(window);
		GLContext.createFromCurrent();

		device = new OpenGLRenderDevice(width, height);
		frameBuffer = new RenderContext(device);
		input = new OpenGLInput(window);
		audioDevice = new OpenALAudioDevice();
	}

	@Override
	public void swapBuffers() {
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
