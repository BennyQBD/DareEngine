/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import engine.rendering.IRenderDevice.BlendMode;

/**
 * Represents a location that can be rendered to by a particular {@link IRenderDevice}
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class RenderTarget {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private int fbo;

	/**
	 * Creates a new render target.
	 * 
	 * @param device The device rendering to this target.
	 * @param width The width of the render target.
	 * @param height The height of the render target.
	 * @param texId The integer id of the texture being rendered to in the device.
	 */
	public RenderTarget(IRenderDevice device, int width, int height, int texId) {
		this(device, width, height, texId, device.createRenderTarget(width,
				height, texId));
	}

	/**
	 * Creates a new render target based on an existing target in the device.
	 * 
	 * @param device The device rendering to this target.
	 * @param width The width of the render target.
	 * @param height The height of the render target.
	 * @param texId The integer id of the texture being rendered to in the device.
	 * @param fbo The integer id of the render target already existing on the device.
	 */
	public RenderTarget(IRenderDevice device, int width, int height, int texId,
			int fbo) {
		super();
		this.device = device;
		this.width = width;
		this.height = height;
		this.fbo = fbo;
	}

	@Override
	public void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	/**
	 * Releases this object. This should be called when the object will no
	 * longer be used, and no methods or fields should be used after this method
	 * is called.
	 */
	public void dispose() {
		fbo = device.releaseRenderTarget(fbo);
	}

	/**
	 * Gets the width of this render target.
	 * 
	 * @return The width of this render target.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of this render target.
	 * 
	 * @return The height of this render target.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Clears the render target to a single color.
	 * 
	 * @param color
	 *            The color to clear to.
	 */
	public void clear(Color color) {
		device.clear(fbo, color);
	}

	/**
	 * Produces a rendering effect within a rectangle.
	 * 
	 * @param texId
	 *            The integer identifying the texture being used for rendering.
	 * @param mode
	 *            The blend mode being used
	 * @param startX
	 *            The start location on X, normalized into the range (-1, 1)
	 * @param startY
	 *            The start location on Y, normalized into the range (-1, 1)
	 * @param endX
	 *            The end location on X, normalized into the range (-1, 1)
	 * @param endY
	 *            The end location on Y, normalized into the range (-1, 1)
	 * @param texStartX
	 *            The location in the image on X where pixels should begin being
	 *            copied, normalized into the range (0, 1)
	 * @param texStartY
	 *            The location in the image on Y where pixels should begin being
	 *            copied, normalized into the range (0, 1)
	 * @param texEndX
	 *            The location in the image on X where pixels should finish
	 *            being copied, normalized into the range (0, 1)
	 * @param texEndY
	 *            The location in the image on Y where pixels should finish
	 *            being copied, normalized into the range (0, 1)
	 * @param c
	 *            The color masking everything.
	 * @param transparency
	 *            The amount of transparency being used.
	 */
	public void drawRect(int texId, BlendMode mode, double startX,
			double startY, double endX, double endY, double texStartX,
			double texStartY, double texEndX, double texEndY, Color c,
			double transparency) {
		device.drawRect(fbo, texId, mode, startX, startY, endX, endY,
				texStartX, texStartY, texEndX, texEndY, c, transparency);
	}
}
