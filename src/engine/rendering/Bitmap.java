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
package engine.rendering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * Stores a set of pixels in a component-based format. The component-based
 * format stores colors as follows:
 *
 * Byte 0: Alpha Byte 1: Blue Byte 2: Green Byte 3: Red
 *
 * This format is fast, compact, and ideal for software rendering. It has the
 * following key advantages: - Entire images can be copied to the screen with a
 * single call to System.arrayCopy. (If the screen is not in ABGR pixel format,
 * it requires some conversion. However, the conversion is typically quick and
 * simple). - Per component operations, such as lighting, can be performed
 * cheaply without any pixel format converison.
 *
 * This class is primarily intended to be a high-performance image storing
 * facility for software rendering. As such, there are points where ease of use
 * is compromised for the sake of performance. If you need to store and use
 * images outside of a software renderer, it is recommended that you use Java's
 * standard image classes instead.
 */
public class Bitmap {
	/** The width, in pixels, of the image */
	private final int width;
	/** The height, in pixels, of the image */
	private final int height;
	/** Every pixel component in the image */
	protected final byte components[];

	/**
	 * Creates and initializes a Bitmap.
	 *
	 * @param width
	 *            The width, in pixels, of the image.
	 * @param height
	 *            The height, in pixels, of the image.
	 */
	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.components = new byte[width * height * 4];
	}

	public Bitmap(String fileName) {
		int width = 0;
		int height = 0;
		byte[] components = null;

		try {
			BufferedImage image = ImageIO.read(Bitmap.class
					.getResource(fileName.substring(5)));
			// ImageIO.read(new File(fileName));

			width = image.getWidth();
			height = image.getHeight();

			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			components = new byte[width * height * 4];

			for (int i = 0; i < width * height; i++) {
				components[i * 4] = (byte) ((pixels[i] >> 24) & 0xFF);
				components[i * 4 + 1] = (byte) ((pixels[i] >> 0) & 0xFF);
				components[i * 4 + 2] = (byte) ((pixels[i] >> 8) & 0xFF);
				components[i * 4 + 3] = (byte) ((pixels[i] >> 16) & 0xFF);
				// int pixel = pixels[i];
				// int alpha = (~pixel) & 0xFF000000;
				// int color = pixel & 0x00FFFFFF;
				// pixels[i] = alpha | color;
			}
		} catch (IOException e) {
			//TODO: Proper Error Handling!
			e.printStackTrace();
			System.exit(1);
		}

		this.width = width;
		this.height = height;
		this.components = components;
	}

	/**
	 * Sets every pixel in the bitmap to a specific shade of grey.
	 */
	public void clear(byte shade) {
		Arrays.fill(components, shade);
	}

	/**
	 * Sets the pixel at (x, y) to the color specified by (a,b,g,r).
	 */
	public void drawPixel(int x, int y, byte a, byte b, byte g,
			byte r) {
		int index = (x + y * width) * 4;
		components[index] = a;
		components[index + 1] = b;
		components[index + 2] = g;
		components[index + 3] = r;
	}

	/**
	 * Copies the Bitmap into a BGR byte array.
	 */
	public void copyToByteArray(byte[] dest) {
		for (int i = 0; i < width * height; i++) {
			dest[i * 3] = components[i * 4 + 1];
			dest[i * 3 + 1] = components[i * 4 + 2];
			dest[i * 3 + 2] = components[i * 4 + 3];
		}
	}

	public void copyNearest(Bitmap dest, int destX, int destY,
			float srcXFloat, float srcYFloat) {
		int srcX = (int) (srcXFloat * (getWidth() - 1));
		int srcY = (int) (srcYFloat * (getHeight() - 1));

		int destIndex = (destX + destY * dest.getWidth()) * 4;
		int srcIndex = (srcX + srcY * getWidth()) * 4;

		dest.setComponent(destIndex, components[srcIndex]);
		dest.setComponent(destIndex + 1,
				components[srcIndex + 1]);
		dest.setComponent(destIndex + 2,
				components[srcIndex + 2]);
		dest.setComponent(destIndex + 3,
				components[srcIndex + 3]);
	}

	public Bitmap clearScreen(byte a, byte b, byte g, byte r) {
		for (int i = 0; i < getWidth() * getHeight(); i++) {
			setComponent(i * 4, a);
			setComponent(i * 4 + 1, b);
			setComponent(i * 4 + 2, g);
			setComponent(i * 4 + 3, r);
		}

		return this;
	}

	public byte getNearestComponent(float srcXFloat,
			float srcYFloat, int component) {
		int srcX = (int) (srcXFloat * (getWidth() - 1));
		int srcY = (int) (srcYFloat * (getHeight() - 1));

		int srcIndex = (srcX + srcY * getWidth()) * 4;

		return components[srcIndex + component];
	}

	/** Basic getter */
	public int getWidth() {
		return width;
	}

	/** Basic getter */
	public int getHeight() {
		return height;
	}

	public float getAspect() {
		return (float) width / (float) height;
	}

	public byte getComponent(int location) {
		return components[location];
	}

	public void setComponent(int location, byte value) {
		components[location] = value;
	}
}
