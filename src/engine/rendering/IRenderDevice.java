/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import static org.lwjgl.opengl.GL11.*;

/**
 * Interface for some piece of hardware, physical or virtual, that is capable of
 * graphics rendering.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IRenderDevice {
	/**
	 * Specifies a way of blending graphics.
	 */
	public static enum BlendMode {
		SPRITE, ADD_LIGHT, APPLY_LIGHT
	}

	/** Filter images using color from the pixel nearest to desired point. */
	public static final int FILTER_NEAREST = GL_NEAREST;
	/**
	 * Filter images by sampling color from several pixels near the given point
	 * and linearly interpolating to more accurately determine color at a given
	 * point
	 */
	public static final int FILTER_LINEAR = GL_LINEAR;

	/**
	 * Releases any resources being used. This object should not be used after
	 * this is called.
	 */
	public void dispose();

	/**
	 * Creates a texture that can be used by the device. It is preferred to use
	 * this through the {@link Texture} class when possible.
	 * 
	 * @param width
	 *            The width of the texture, in pixels.
	 * @param height
	 *            The height of the texture, in pixels.
	 * @param image
	 *            The image being used as a texture.
	 * @param filter
	 *            The filter used when sampling the image. Should be one of the
	 *            IRenderDevice.FILTER options.
	 * @return An integer identifying the texture on this device.
	 */
	public int createTexture(int width, int height, ArrayBitmap image,
			int filter);

	/**
	 * Releases and invalidates a texture. The value of {@code id} will be
	 * invalid after this call and may be reused to identify a new texture. Note
	 * that this does nothing when the null id, 0, is given for {@code id}.
	 * <p/>
	 * It is preferred to use this through the {@link Texture} class when
	 * possible.
	 * 
	 * @param id
	 *            The integer identifying the texture to be released.
	 * @return The null id of 0.
	 */
	public int releaseTexture(int id);

	/**
	 * Gets a texture, in whole or in part, from this device. It is preferred to
	 * use this through the {@link Texture} class when possible.
	 * 
	 * @param id
	 *            The integer identifying the texture to be gotten.
	 * @param x
	 *            The x location to start getting the texture from.
	 * @param y
	 *            The y location to start getting the texture from.
	 * @param width
	 *            How many pixels should be read on x.
	 * @param height
	 *            How many pixels should be read on y.
	 * @return An ArrayBitmap storing the read portion of the texture.
	 */
	public ArrayBitmap getTexture(int id, int x, int y, int width, int height);

	/**
	 * Creates a render target that can be used by this device. It is preferred
	 * to use this through the {@link RenderTarget} class when possible.
	 * 
	 * @param width
	 *            The width of the render target, in pixels.
	 * @param height
	 *            The height of the render target, in pixels.
	 * @param texId
	 *            The integer identifying the texture being used for rendering.
	 * @return An integer identifying the render target on this device.
	 */
	public int createRenderTarget(int width, int height, int texId);

	/**
	 * Releases and invalidates a render target. The value of {@code fbo} will
	 * be invalid after this call and may be reused to identify a new render
	 * target. Note that this does nothing when the null id, 0, is given for
	 * {@code fbo}.
	 * <p/>
	 * It is preferred to use this through the {@link RenderTarget} class when
	 * possible.
	 * 
	 * @param fbo
	 *            The integer identifying the render target to be released.
	 * @return The null id of 0.
	 */
	public int releaseRenderTarget(int fbo);

	/**
	 * Clears a render target to a single color. It is preferred to use this
	 * through the {@link RenderTarget} class when possible.
	 * 
	 * @param fbo
	 *            The integer identifying the render target.
	 * @param color
	 *            The color to clear to.
	 */
	public void clear(int fbo, Color color);

	/**
	 * Produces a rendering effect within a rectangle. It is preferred to use
	 * this through the {@link RenderTarget} class when possible.
	 * 
	 * @param fbo
	 *            The integer identifying the render target.
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
	public void drawRect(int fbo, int texId, BlendMode mode, double startX,
			double startY, double endX, double endY, double texStartX,
			double texStartY, double texEndX, double texEndY, Color c,
			double transparency);
}
