package engine.rendering.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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

	public OpenGLDisplay(int width, int height, String title) throws LWJGLException {
		Display.setTitle(title);

		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();
		Keyboard.create();
		Mouse.create();

		Display.setVSyncEnabled(!Debug.IGNORE_FRAME_CAP);
		device = new OpenGLRenderDevice(width, height);
		frameBuffer = new RenderContext(device);
		input = new OpenGLInput();
		audioDevice = new OpenALAudioDevice();
	}

	@Override
	public void swapBuffers() {
		Display.update();
	}

	@Override
	public boolean isClosed() {
		return Display.isCloseRequested();
	}

	@Override
	public void dispose() {
		device.dispose();
		audioDevice.dispose();
		frameBuffer.dispose();
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
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
