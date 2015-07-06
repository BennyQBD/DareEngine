/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.util.Util;

/**
 * Stores images in an array of 32-bit ARGB pixels.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class ArrayBitmap {
	/**
	 * Interface for visiting every pixel in an ArrayBitmap.
	 */
	public static interface IVisitor {
		/**
		 * Function that is called for every pixel in the bitmap.
		 * 
		 * @param x
		 *            The x coordinate of the pixel.
		 * @param y
		 *            The y coordinate of the pixel.
		 * @param pixel
		 *            The 32-bit ARGB color of the pixel.
		 */
		public void visit(int x, int y, int pixel);
	}

	private final int offsetX;
	private final int offsetY;
	private final int rowOffset;
	private final int width;
	private final int height;
	private final int[] pixels;

	/**
	 * Creates a new ArrayBitmap
	 * 
	 * @param width
	 *            The width, in pixels, of the bitmap.
	 * @param height
	 *            The height, in pixels, of the bitmap.
	 */
	public ArrayBitmap(int width, int height) {
		this(width, height, new int[width * height]);
	}

	/**
	 * Creates a new ArrayBitmap from an existing array of 32-bit ARGB pixels.
	 * 
	 * @param width
	 *            The width, in pixels, of the bitmap.
	 * @param height
	 *            The height, in pixels, of the bitmap.
	 * @param pixels
	 *            The array of 32-bit ARGB pixels to be used.
	 */
	public ArrayBitmap(int width, int height, int[] pixels) {
		this(width, height, pixels, 0, 0, width);
	}

	/**
	 * Creates a new ArrayBitmap from a subset of an existing array of 32-bit
	 * ARGB pixels.
	 * 
	 * @param width
	 *            The width, in pixels, of the bitmap.
	 * @param height
	 *            The height, in pixels, of the bitmap.
	 * @param pixels
	 *            The array of 32-bit ARGB pixels to be used.
	 * @param offsetX
	 *            The start of the image in the {@code pixels} on X.
	 * @param offsetY
	 *            The start of the image in the {@code pixels} on Y.
	 * @param rowOffset
	 *            The length of one row of pixels in the {@code pixels} array.
	 */
	public ArrayBitmap(int width, int height, int[] pixels, int offsetX,
			int offsetY, int rowOffset) {
		this.width = width;
		this.height = height;
		if (pixels.length < getNumPixels()) {
			throw new IllegalArgumentException(
					"Pixel array is smaller than width and height specify");
		}
		this.pixels = pixels;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.rowOffset = rowOffset;
		if (getIndex(width - 1, height - 1) > pixels.length) {
			throw new IllegalArgumentException(
					"Pixel array is not big enough to fit "
							+ "width, height, offsetX, offsetY, and rowOffset combination");
		}
	}

	/**
	 * Create an ArrayBitmap from a file
	 * 
	 * @param fileName
	 *            The path and name of a file containing an image.
	 * @throws IOException
	 *             If the file cannot be loaded.
	 */
	public ArrayBitmap(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = new int[getNumPixels()];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		this.offsetX = 0;
		this.offsetY = 0;
		this.rowOffset = width;
	}

	/**
	 * Saves an ArrayBitmap to a file.
	 * 
	 * @param fileName
	 *            The path and name of the file to save to.
	 * @param outputFormat
	 *            The image format to be used. For example, "png" and "jpg" are
	 *            valid values.
	 * @throws IOException
	 *             If the image cannot be saved.
	 */
	public void save(String fileName, String outputFormat) throws IOException {
		BufferedImage output = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] data = ((DataBufferInt) output.getRaster().getDataBuffer())
				.getData();
		for (int i = 0; i < data.length; i++) {
			data[i] = pixels[i];
		}
		ImageIO.write(output, outputFormat, new File(fileName));
	}

	/**
	 * Visits every pixel in the image.
	 * 
	 * @param visitor
	 *            To be called at each pixel visited.
	 */
	public void visitAll(IVisitor visitor) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				visitor.visit(i, j, get(i, j));
			}
		}
	}

	/**
	 * Sets every pixel in the image to the same 32-bit ARGB color.
	 * 
	 * @param color
	 *            The 32-bit ARGB color to set every pixel in the image to.
	 */
	public void clear(int color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				set(i, j, color);
			}
		}
	}

	/**
	 * Gets the width of this image.
	 * 
	 * @return The width of this image.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of this image.
	 * 
	 * @return The height of this image.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns true if the the pixel's alpha is more than 0.5.
	 * 
	 * @param x
	 *            The X location of the pixel.
	 * @param y
	 *            The Y location of the pixel.
	 * @return True if the pixel's alpha is more than 0.5.
	 */
	public boolean isMoreOpaqueThanTransparent(int x, int y) {
		boundsCheck(x, y);
		// This works because in ARGB colors, the alpha has the sign bit. If
		// alpha is > 128, then as an integer, the color will be less than 0
		// because the sign bit will be set.
		return get(x, y) < 0;
	}

	/**
	 * Get a pixel at a specific location.
	 * 
	 * @param x
	 *            The X location of the pixel.
	 * @param y
	 *            The Y location of the pixel.
	 * @return The pixel at location (x, y).
	 */
	public int get(int x, int y) {
		boundsCheck(x, y);
		return pixels[getIndex(x, y)];
	}

	/**
	 * Sets a pixel at a specific location.
	 * 
	 * @param x
	 *            The X location of the pixel.
	 * @param y
	 *            The Y location of the pixel.
	 * @param pixel
	 *            The 32-bit ARGB color to set the pixel to.
	 */
	public void set(int x, int y, int pixel) {
		boundsCheck(x, y);
		pixels[getIndex(x, y)] = pixel;
	}

	private int getIndex(int x, int y) {
		return (x + offsetX) + (y + offsetY) * rowOffset;
	}

	private int getNumPixels() {
		return width * height;
	}

	private void boundsCheck(int x, int y) {
		Util.boundsAssert(x, 0, width - 1);
		Util.boundsAssert(y, 0, height - 1);
	}
}
