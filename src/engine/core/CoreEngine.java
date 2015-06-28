package engine.core;

import engine.rendering.IDisplay;
import engine.util.Debug;

public class CoreEngine {
	private final IDisplay display;
	private final double frameTime;
	private final Scene scene;

	public CoreEngine(IDisplay display, Scene scene, double frameRate) {
		this.frameTime = 1.0 / frameRate;
		this.display = display;
		this.scene = scene;
	}

	public void dispose() {
		display.dispose();
	}

	@SuppressWarnings("unused")
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
				render = true;
				boolean shouldExit = scene.update(frameTime);
				if (shouldExit) {
					isRunning = false;
				}
				unprocessedTime -= frameTime;
			}

			if (render || Debug.IGNORE_FRAME_CAP) {
				frames++;
				scene.render(display.getRenderContext());
				display.swapBuffers();
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
