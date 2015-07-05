/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

/**
 * Interface that specifies higher-level rendering functions for a rendering
 * device.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IRenderContext {
	/**
	 * Clears to a specific color.
	 * 
	 * @param color
	 *            The color to clear to.
	 */
	public void clear(Color color);

	/**
	 * Draws a sprite.
	 * 
	 * @param sheet
	 *            The {@link SpriteSheet} containing the sprite
	 * @param index
	 *            The index of the sprite in the {@link SpriteSheet}.
	 * @param startX
	 *            The start location on X, normalized into the range (-1, 1)
	 * @param startY
	 *            The start location on Y, normalized into the range (-1, 1)
	 * @param endX
	 *            The end location on X, normalized into the range (-1, 1)
	 * @param endY
	 *            The end location on Y, normalized into the range (-1, 1)
	 * @param transparency
	 *            The amount of transparency the sprite should be drawn with.
	 * @param flipX
	 *            Whether the sprite is flipped on the x axis.
	 * @param flipY
	 *            Whether the sprite is flipped on the y axis.
	 * @param color
	 *            The color masking the sprite.
	 */
	public void drawSprite(SpriteSheet sheet, int index, double startX,
			double startY, double endX, double endY, double transparency,
			boolean flipX, boolean flipY, Color color);

	/**
	 * Draws a string.
	 * 
	 * @param msg
	 *            The string to draw
	 * @param font
	 *            The {@link SpriteSheet} containing the font being used.
	 * @param x
	 *            The start location on X, normalized into the range (-1, 1)
	 * @param y
	 *            The start location on Y, normalized into the range (-1, 1)
	 * @param scale
	 *            How large each character should be
	 * @param color
	 *            The color being used for the font
	 * @param wrapX
	 *            The point on x at which the text should wrap to the next line.
	 *            -1 if no wrapping is desired.
	 * @return The y location that the next line of text should begin at.
	 */
	public double drawString(String msg, SpriteSheet font, double x, double y,
			double scale, Color color, double wrapX);

	/**
	 * Clears all lighting to a specific color. Note that lighting is deferred
	 * until the applyLighting method is called.
	 * 
	 * @param color
	 *            The color to clear to.
	 */
	public void clearLighting(Color color);

	/**
	 * Draws a light to the screen. Note that lighting is deferred until the
	 * applyLighting method is called.
	 * 
	 * @param light
	 *            The light to draw
	 * @param startX
	 *            The start location on X, normalized into the range (-1, 1)
	 * @param startY
	 *            The start location on Y, normalized into the range (-1, 1)
	 * @param endX
	 *            The end location on X, normalized into the range (-1, 1)
	 * @param endY
	 *            The end location on Y, normalized into the range (-1, 1)
	 * @param texStartX
	 *            The location in the {@link LightMap} on X where pixels should
	 *            begin being copied, normalized into the range (0, 1)
	 * @param texStartY
	 *            The location in the {@link LightMap} on Y where pixels should
	 *            begin being copied, normalized into the range (0, 1)
	 * @param texEndX
	 *            The location in the {@link LightMap} on X where pixels should
	 *            finish being copied, normalized into the range (0, 1)
	 * @param texEndY
	 *            The location in the {@link LightMap} on Y where pixels should
	 *            finish being copied, normalized into the range (0, 1)
	 * @param color
	 *            The color to mask the light with.
	 */
	public void drawLight(LightMap light, double startX, double startY,
			double endX, double endY, double texStartX, double texStartY,
			double texEndX, double texEndY, Color color);

	/**
	 * Applies all accumulated lighting operations to whatever has currently
	 * been drawn. This function should typically be called after most
	 * rendering, but before text and GUI rendering, so that they are unaffected
	 * by lighting.
	 */
	public void applyLighting();

	/**
	 * Releases any resources being used. This object should not be used after
	 * this is called.
	 */
	void dispose();
}
