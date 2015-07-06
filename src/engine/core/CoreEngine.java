/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.core;

import engine.rendering.IDisplay;
import engine.util.Debug;

/**
 * The core game engine. Updates and renders a scene in a display at a
 * consistent rate.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class CoreEngine {
	private final IDisplay display;
	private final double frameTime;
	private final Scene scene;

	/**
	 * Creates a new CoreEngine in a usable state.
	 * 
	 * @param display
	 *            The display to render the game in.
	 * @param scene
	 *            The game scene that the engine should run.
	 * @param frameRate
	 *            The number of frames to be rendered per second, on average.
	 */
	public CoreEngine(IDisplay display, Scene scene, double frameRate) {
		this.frameTime = 1.0 / frameRate;
		this.display = display;
		this.scene = scene;
	}

	/**
	 * Begins running the game engine and all it's various components
	 */
	public void start() {
		int frames = 0;
		double unprocessedTime = 0.0;
		double frameCounterTime = 0;

		long previousTime = System.nanoTime();
		String fpsString = "0 ms per frame (0 fps)";

		boolean isRunning = true;
		while (!display.isClosed() && isRunning) {
			boolean render = false;

			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;

			unprocessedTime += passedTime / 1000000000.0;
			frameCounterTime += passedTime / 1000000000.0;

			if (frameCounterTime >= 1.0) {
				fpsString = (1000.0 / frames) + " ms per frame (" + frames
						+ " fps)";
				Debug.log(fpsString);

				frames = 0;
				frameCounterTime = 0.0;
			}

			while (unprocessedTime > frameTime) {
				display.update();
				render = true;
				boolean shouldExit = scene.update(frameTime);
				if (shouldExit) {
					isRunning = false;
				}
				unprocessedTime -= frameTime;
			}

			if (render || Debug.isIgnoringFrameCap()) {
				frames++;
				scene.render(display.getRenderContext());
				display.present();
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
