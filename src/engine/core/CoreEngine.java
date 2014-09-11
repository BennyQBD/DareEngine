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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.rendering.RenderContext;

/**
 * The core game engine.
 * <p>
 * Keeps track of the various game engine components, and controls when they
 * take action.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class CoreEngine extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	/**
	 * If true, game is rendered as often as possible, rather than staying at a
	 * fixed framerate, such as 60 frames per second.
	 */
	private static final boolean IGNORE_FRAMECAP = false;

	/** The primary thread of execution */
	private final Thread thread;
	/* The scene that the engine is running */
	private final Scene scene;
	/** Whether the engine is currently running or not */
	private boolean isRunning;

	/** The window being used for display */
	private JFrame frame;
	/** The bitmap representing the final image to display */
	private final RenderContext frameBuffer;
	/** Used to display the framebuffer in the window */
	private final BufferedImage displayImage;
	/** The pixels of the display image, as an array of byte components */
	private final byte[] displayComponents;
	/** The buffers in the Canvas */
	private BufferStrategy bufferStrategy;
	/** A graphics object that can draw into the Canvas's buffers */
	private Graphics graphics;
	/** The user input received by this display. */
	private final Input input;
	
	private final float frameRate;

	/**
	 * Initializes the CoreEngine to a usable state.
	 *
	 * @param width
	 *            The width of the game window, in pixels
	 * @param height
	 *            The height of the game window, in pixels
	 * @param scene
	 *            The game scene that the engine should run.
	 */
	public CoreEngine(int width, int height, float frameRate, Scene scene) {
		this.frameRate = frameRate;
		this.scene = scene;
		this.thread = new Thread(this);
		this.isRunning = false;

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		// Creates images used for display.
		this.frameBuffer = new RenderContext(width, height);
		this.displayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		this.displayComponents = ((DataBufferByte) displayImage
				.getRaster().getDataBuffer()).getData();
		this.frameBuffer.clear((byte) 0x00);
		this.input = new Input();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		setFocusable(true);
		requestFocus();
	}

	/**
	 * Creates a window to display the game
	 * 
	 * @param title
	 *            The text in the window's title bar.
	 * @param center
	 *            Whether the window should be in the center or not
	 */
	public void createWindow(String title, boolean center) {
		// Create a JFrame designed specifically to show this Display.
		frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);

		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		if (!center) {
			frame.setLocation(0, 0);
		}
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle(title);

		// this.frame = new JFrame();
		// this.frame.setResizable(false);
		// this.frame.add(this);
		// this.frame.pack();
		// this.frame.setTitle(title);
		// this.frame
		// .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// // m_frame.setSize(width, height);
		// this.frame.setLocationRelativeTo(null);
		// if (!center) {
		// this.frame.setLocation(0, 0);
		// }
		// this.frame.setVisible(true);
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
		double secondsPerFrame = 1.0 / frameRate;
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

				scene.update(input, (float) secondsPerFrame);
				unprocessedTime -= secondsPerFrame;
			}

			// Only render if it is actually needed.
			if (render || IGNORE_FRAMECAP) {
				frames++;

				scene.render(frameBuffer);
				swapBuffers();
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

	/**
	 * Presents the user with any new images drawn in the display.
	 */
	private void swapBuffers() {
		// The bufferStrategy is allocated lazily; otherwise issues
		// can arise when using an applet.
		if (bufferStrategy == null) {
			// Allocates 1 display buffer, and gets access to it via the buffer
			// strategy and a graphics object for drawing into it.
			createBufferStrategy(2);
			this.bufferStrategy = getBufferStrategy();
			this.graphics = bufferStrategy.getDrawGraphics();
		}

		// Display components should be the byte array used for displayImage's
		// pixels. Therefore, this call should effectively copy the frameBuffer
		// into the displayImage.
		frameBuffer.copyToByteArray(displayComponents);
		graphics.drawImage(displayImage, 0, 0,
				frameBuffer.getWidth(), frameBuffer.getHeight(),
				null);
		bufferStrategy.show();
	}
}
