/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import engine.audio.IAudioDevice;
import engine.input.IInput;

/**
 * Interface for a display system showing the game.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IDisplay {
	/**
	 * Handle any events the display may have recieved since previous updates
	 */
	public void update();

	/**
	 * Presents a rendered frame in the display.
	 */
	public void present();

	/** Returns true if this display has been closed */
	public boolean isClosed();

	/**
	 * Releases any resources being used. This object should not be used after this is
	 * called.
	 */
	public void dispose();

	/**
	 * Gets the render context that can be used with this display.
	 * @return The render context that can be used with this display.
	 */
	public IRenderContext getRenderContext();

	/**
	 * Gets the render device that can be used with this display.
	 * @return The render device that can be used with this display.
	 */
	public IRenderDevice getRenderDevice();

	/**
	 * Gets the input that can be used with this display.
	 * @return The input that can be used with this display.
	 */
	public IInput getInput();

	/**
	 * Gets the audio device that can be used with this display.
	 * @return The audio device that can be used with this display.
	 */
	public IAudioDevice getAudioDevice();
}
