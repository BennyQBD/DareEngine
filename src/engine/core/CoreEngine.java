/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.core;

import engine.rendering.RenderContext;

/**
 * The core game engine.
 * <p>
 * Keeps track of the various game engine components, and controls when they
 * take action.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 * @version 1.0
 * @since 2014-09-10
 */
public class CoreEngine implements Runnable {
	/**
	 * If true, game is rendered as often as possible, rather than staying at a
	 * fixed framerate, such as 60 frames per second.
	 */
	private static final boolean IGNORE_FRAMECAP = false;

	/** The primary thread of execution */
	private final Thread thread;
	/* Where any graphics are displayed */
	private final Display display;
	/* The scene that the engine is running */
	private final Scene scene;
	/** Whether the engine is currently running or not */
	private boolean isRunning;

	/**
	 * Initializes the CoreEngine to a usable state.
	 * 
	 * @param display
	 *            Where any graphics are displayed.
	 * @param scene
	 *            The game scene that the engine should run.
	 */
	public CoreEngine(Display display, Scene scene) {
		this.display = display;
		this.scene = scene;
		this.thread = new Thread(this);
		this.isRunning = false;

		// Display something immediately; helps reduce first frame issues.
		display.swapBuffers();
		display.swapBuffers();
	}

	/**
	 * Begins running the game engine and all it's various components. Can be
	 * stopped by calling the {@link #stop()} method.
	 * 
	 * @see #stop()
	 */
	public void start() {
		if (isRunning) {
			return;
		}
		isRunning = true;

		thread.start();
	}

	/**
	 * Stops running the game engine and all it's various components. Can be
	 * started by calling the {@link #start()} method.
	 * 
	 * @see #start()
	 */
	public void stop() {
		if (!isRunning) {
			return;
		}
		isRunning = false;

		try {
			thread.join();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * The primary function that runs the game engine and all it's components.
	 * <p>
	 * This should not called directly. It is only public because the Runnable
	 * interface requires it to be.
	 * 
	 * If you wish to run the engine, use the {@link #start()} method instead.
	 * 
	 * @see #start()
	 */
	public void run() {
		int frames = 0;
		double unprocessedTime = 0.0;
		// TODO: Don't hardcode framerate at 60 fps.
		double secondsPerFrame = 1.0 / 60.0;
		double frameCounterTime = 0;

		long previousTime = System.nanoTime();
		String fpsString = "0 ms per frame (0 fps)";
		while (isRunning) {
			boolean render = false;

			// Update current and passed time based on time since
			// last frame
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;

			// Update time variables based on passed time
			unprocessedTime += passedTime / 1000000000.0;
			frameCounterTime += passedTime / 1000000000.0;

			// If 1 second has passed since fps was displayed,
			// then display it.
			if (frameCounterTime >= 1.0) {
				fpsString = (1000.0 / frames)
						+ " ms per frame (" + frames + " fps)";
				System.out.println(fpsString);

				frames = 0;
				frameCounterTime = 0.0;
			}

			// As long as at least secondsPerFrame time has passed
			// since the last update, keep updating.
			//
			// This simulates a fixed time delta of secondsPerFrame,
			// which can both maintain stable performance and prevent
			// simulation errors from excessively large or small deltas.
			while (unprocessedTime > secondsPerFrame) {
				render = true;

				scene.Update(display.getInput(),
						(float) secondsPerFrame);
				unprocessedTime -= secondsPerFrame;
			}

			// Only render if it is actually needed.
			if (render || IGNORE_FRAMECAP) {
				frames++;

				RenderContext context = display.getContext();
				scene.Render(context);
				display.swapBuffers();
			} else {
				// If no rendering is needed, let the processor
				// perform other tasks for a while.
				try {
					Thread.sleep(1);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}
	}
}
