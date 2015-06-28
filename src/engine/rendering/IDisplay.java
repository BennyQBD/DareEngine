package engine.rendering;

import engine.audio.IAudioDevice;
import engine.input.IInput;

public interface IDisplay {
	public void swapBuffers();

	public boolean isClosed();
	public void dispose();
	
	public IRenderContext getRenderContext();
	public IRenderDevice getRenderDevice();
	public IInput getInput();
	public IAudioDevice getAudioDevice();
}
