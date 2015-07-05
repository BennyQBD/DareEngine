/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

/**
 * Represents a renderable texture.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Texture {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private int id;

	/**
	 * Creates a texture that can be rendered by a particular
	 * {@link IRenderDevice}.
	 * 
	 * @param device
	 *            The device the texture is being created for.
	 * @param image
	 *            The image being converted into a texture.
	 * @param filter
	 *            The type of filtering to be used. Should be one of the
	 *            IRenderDevice.FILTER options.
	 */
	public Texture(IRenderDevice device, ArrayBitmap image, int filter) {
		this.device = device;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.id = device.createTexture(width, height, image, filter);
	}

	/**
	 * Releases this object. This should be called when the object will no
	 * longer be used, and no methods or fields should be used after this method
	 * is called.
	 */
	public void dispose() {
		id = device.releaseTexture(id);
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	/**
	 * Gets the integer identifying this texture on it's associated
	 * {@link IRenderDevice}
	 * 
	 * @return The integer identifying this texture on it's associated
	 *         {@link IRenderDevice}
	 */
	public int getDeviceID() {
		return id;
	}

	/**
	 * Gets the width of this texture.
	 * 
	 * @return The width of this texture.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of this texture.
	 * 
	 * @return The height of this texture.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets an ArrayBitmap containing the pixels of this texture.
	 * 
	 * @return An ArrayBitmap containing the pixels of this texture.
	 */
	public ArrayBitmap getPixels() {
		return getPixels(0, 0, width, height);
	}

	/**
	 * Gets an ArrayBitmap containing the pixels of part of this texture.
	 * 
	 * @param x
	 *            The x location to start getting the texture from.
	 * @param y
	 *            The y location to start getting the texture from.
	 * @param width
	 *            How many pixels should be read on x.
	 * @param height
	 *            How many pixels should be read on y.
	 * @return An ArrayBitmap containing the pixels of part of this texture.
	 */
	public ArrayBitmap getPixels(int x, int y, int width, int height) {
		return device.getTexture(id, x, y, width, height);
	}
}
