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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * Stores a set of pixels in a component-based format. The component-based
 * format stores colors as follows:
 * <p>
 * Byte 0: Alpha <br>
 * Byte 1: Blue <br>
 * Byte 2: Green <br>
 * Byte 3: Red<br>
 * <p>
 * This format is fast, compact, and ideal for software rendering. It has the
 * following key advantages:
 * <p>
 * - Entire images can be copied to the screen with a single call to
 * System.arrayCopy. (If the screen is not in ABGR pixel format, it requires
 * some conversion. However, the conversion is typically quick and simple).<br>
 * - Per component operations, such as lighting, can be performed cheaply
 * without any pixel format conversion.
 * <p>
 * This class is primarily intended to be a high-performance image storing
 * facility for software rendering. As such, there are points where ease of use
 * is compromised for the sake of performance. If you need to store and use
 * images outside of a software renderer, it is recommended that you use Java's
 * standard image classes instead.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Bitmap {
	/** The width, in pixels, of the image */
	private final int width;
	/** The height, in pixels, of the image */
	private final int height;
	/** Every pixel component in the image */
	protected final byte components[];

	/**
	 * Generates a Bitmap with a checkerboard pattern.
	 * <p>
	 * This will generate a new bitmap every call. This is intended to prevent
	 * issues with modifying part of the default Bitmap, and having that affect
	 * other users of default bitmaps.
	 * <p>
	 * Note that this method is intended primarily for debugging usage.
	 * 
	 * @return Bitmap with checkerboard pattern.
	 */
	public static Bitmap getNewDefaultBitmap() {
		Bitmap defaultBitmap = new Bitmap(8, 8);

		// Generate checkerboard pattern
		for (int j = 0; j < defaultBitmap.getHeight(); j++) {
			for (int i = 0; i < defaultBitmap.getWidth(); i++) {
				if ((i + j) % 2 == 0) {
					defaultBitmap.drawPixel(i, j, (byte) 0x80,
							(byte) 0xFF, (byte) 0xFF,
							(byte) 0xFF);
				} else {
					defaultBitmap.drawPixel(i, j, (byte) 0x80,
							(byte) 0x00, (byte) 0x39,
							(byte) 0x72);
				}
			}
		}

		return defaultBitmap;
	}

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

	/**
	 * Loads an image into a Bitmap from a file.
	 * 
	 * @param fileName
	 *            The name of the file to be loaded. File paths are relative to
	 *            the resource folder, so a file in
	 *            "C:/myFolder/myProject/projectResourceFolder/textures/myImage.png"
	 *            should be specified as "textures/myImage.png"
	 */
	public Bitmap(String fileName) throws IOException {
		int width = 0;
		int height = 0;
		byte[] components = null;

		try {
			fileName = "/" + fileName;
			URL resource = Bitmap.class.getResource(fileName);
			if (resource == null) {
				throw new FileNotFoundException(fileName
						+ " is not an accessible resource.");
			}
			BufferedImage image = ImageIO.read(resource);

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
			}
		} catch (IOException e) {
			throw e;
		}

		this.width = width;
		this.height = height;
		this.components = components;
	}

	/**
	 * Sets every pixel in the bitmap to a specific shade of grey.
	 * 
	 * @param shade
	 *            The shade of grey to use, where (byte)0x00 is black, and
	 *            (byte)0xFF is white.
	 */
	public void clear(byte shade) {
		Arrays.fill(components, shade);
	}

	/**
	 * Sets the pixel at (x, y) to the color specified by (a,b,g,r).
	 * 
	 * @param x
	 *            The x location in the bitmap to change.
	 * @param y
	 *            The y location in the bitmap to change.
	 * @param a
	 *            The alpha value of the desired color.
	 * @param b
	 *            The blue value of the desired color.
	 * @param g
	 *            The green value of the desired color.
	 * @param r
	 *            The red value of the desired color.
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
	 * 
	 * @param dest
	 *            The byte array to be copied into. This array is assumed to be
	 *            large enough to hold the bitmap.
	 */
	public void copyToByteArray(byte[] dest) {
		for (int i = 0; i < width * height; i++) {
			dest[i * 3] = components[i * 4 + 1];
			dest[i * 3 + 1] = components[i * 4 + 2];
			dest[i * 3 + 2] = components[i * 4 + 3];
		}
	}

	/**
	 * Copies the pixel nearest to the normalized coordinates (srcXFloat,
	 * srcYFloat) into the dest Bitmap at location (destX, destY).
	 * 
	 * @param dest
	 *            The Bitmap being copied into.
	 * @param destX
	 *            The location of the destination pixel in X.
	 * @param destY
	 *            The location of the destination pixel in Y.
	 * @param srcXFloat
	 *            The location of the pixel being copied on X, normalized into
	 *            the range (0, 1)
	 * @param srcYFloat
	 *            The location of the pixel being copied on Y, normalized into
	 *            the range (0, 1)
	 */
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

	/**
	 * Sets every pixel in the Bitmap to a specific color.
	 * 
	 * @param a
	 *            The alpha value of the desired color.
	 * @param b
	 *            The blue value of the desired color.
	 * @param g
	 *            The green value of the desired color.
	 * @param r
	 *            The red value of the desired color.
	 * @return This Bitmap, for programming convenience.
	 */
	public Bitmap clearScreen(byte a, byte b, byte g, byte r) {
		for (int i = 0; i < getWidth() * getHeight(); i++) {
			setComponent(i * 4, a);
			setComponent(i * 4 + 1, b);
			setComponent(i * 4 + 2, g);
			setComponent(i * 4 + 3, r);
		}

		return this;
	}

	/**
	 * Gets the width of the Bitmap, in pixels.
	 * 
	 * @return The width of the Bitmap, in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the Bitmap, in pixels.
	 * 
	 * @return The height of the Bitmap, in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the aspect ratio of the Bitmap.
	 * 
	 * @return The aspect ratio of the Bitmap, in terms of width/height;
	 */
	public float getAspect() {
		return (float) width / (float) height;
	}

	/**
	 * Gets a component at a specific index in the bitmap.
	 * 
	 * @param location
	 *            The index of the component in the bitmap.
	 * @return The component at the location.
	 */
	public byte getComponent(int location) {
		return components[location];
	}

	/**
	 * Sets a component at a specific index in the bitmap
	 * 
	 * @param location
	 *            The index of the component in the bitmap/
	 * @param value
	 *            The value to set the component to.
	 */
	public void setComponent(int location, byte value) {
		components[location] = value;
	}
}
