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

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import engine.rendering.RenderContext;

/**
 * Represents a window that can be drawn in using a software renderer.
 *
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Display extends Canvas {
	private static final long serialVersionUID = 1L;

	/** The window being used for display */
	private final JFrame frame;
	/** The bitmap representing the final image to display */
	private final RenderContext frameBuffer;
	/** Used to display the framebuffer in the window */
	private final BufferedImage displayImage;
	/** The pixels of the display image, as an array of byte components */
	private final byte[] displayComponents;
	/** The buffers in the Canvas */
	private final BufferStrategy bufferStrategy;
	/** A graphics object that can draw into the Canvas's buffers */
	private final Graphics graphics;
	/** The user input received by this display. */
	private final Input input;

	/**
	 * Creates and initializes a new display.
	 *
	 * @param width
	 *            How wide the display is, in pixels.
	 * @param height
	 *            How tall the display is, in pixels.
	 * @param title
	 *            The text displayed in the window's title bar.
	 */
	public Display(int width, int height, String title) {
		// Set the canvas's preferred, minimum, and maximum size to prevent
		// unintentional resizing.
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
		this.frameBuffer.clear((byte) 0x80);
		this.frameBuffer.drawPixel(100, 100, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0xFF);

		// Create a JFrame designed specifically to show this Display.
		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.add(this);
		this.frame.pack();
		this.frame.setTitle(title);
		this.frame
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// m_frame.setSize(width, height);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		this.input = new Input();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		// Allocates 1 display buffer, and gets access to it via the buffer
		// strategy and a graphics object for drawing into it.
		createBufferStrategy(2);
		this.bufferStrategy = getBufferStrategy();
		this.graphics = bufferStrategy.getDrawGraphics();

		setFocusable(true);
		requestFocus();
	}

	/**
	 * Presents the user with any new images drawn in the display.
	 */
	public void swapBuffers() {
		// Display components should be the byte array used for displayImage's
		// pixels. Therefore, this call should effectively copy the frameBuffer
		// into the displayImage.
		frameBuffer.copyToByteArray(displayComponents);
		graphics.drawImage(displayImage, 0, 0,
				frameBuffer.getWidth(), frameBuffer.getHeight(),
				null);
		bufferStrategy.show();
	}
	
	/**
	 * Gets the Input for this display.
	 * 
	 * @return A reference to the user input received by this display.
	 */
	public Input getInput() {
		return input;
	}

	/**
	 * Gets the RenderContext for this display.
	 * 
	 * @return A RenderContext that can draw into this display.
	 */
	public RenderContext getContext() {
		return frameBuffer;
	}
}
