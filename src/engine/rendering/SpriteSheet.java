/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import engine.space.AABB;
import engine.util.Util;

/**
 * A 2D sheet containing various sprites.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SpriteSheet {
	private final Texture sheet;
	private final int spritesPerX;
	private final int spritesPerY;
	private final int spriteWidth;
	private final int spriteHeight;
	private final int borderedSpriteWidth;
	private final int borderedSpriteHeight;
	private final int spriteBorderSize;
	private final AABB[] spriteAABBs;

	/**
	 * Creates a SpriteSheet
	 * 
	 * @param spriteSheet
	 *            The texture containing the sprites
	 * @param spritesPerX
	 *            Number of sprites on the X axis.
	 * @param spritesPerY
	 *            Number of sprites on the Y axis.
	 * @param spriteBorderSize
	 *            The number of pixels bordering each sprite on all sides.
	 */
	public SpriteSheet(Texture spriteSheet, int spritesPerX, int spritesPerY,
			int spriteBorderSize) {
		this.sheet = spriteSheet;
		this.spritesPerX = spritesPerX;
		this.spritesPerY = spritesPerY;
		this.spriteBorderSize = spriteBorderSize;
		this.borderedSpriteWidth = spriteSheet.getWidth() / spritesPerX;
		this.borderedSpriteHeight = spriteSheet.getHeight() / spritesPerY;
		this.spriteWidth = borderedSpriteWidth - 2 * spriteBorderSize;
		this.spriteHeight = borderedSpriteHeight - 2 * spriteBorderSize;

		spriteAABBs = new AABB[getNumSprites()];
		ArrayBitmap pixels = sheet.getPixels();
		for (int i = 0; i < getNumSprites(); i++) {
			spriteAABBs[i] = generateAABB(i, pixels);
		}
	}

	/**
	 * Gets the width of a single sprite.
	 * 
	 * @return The width of a single sprite.
	 */
	public int getSpriteWidth() {
		return spriteWidth;
	}

	/**
	 * Gets the height of a single sprite.
	 * 
	 * @return The height of a single sprite.
	 */
	public int getSpriteHeight() {
		return spriteHeight;
	}

	/**
	 * Gets the aspect ratio of a sprite.
	 * 
	 * @return The aspect ratio of a sprite.
	 */
	public double getSpriteAspect() {
		return (double) spriteWidth / (double) spriteHeight;
	}

	/**
	 * Gets the texture containing the sprite images.
	 * 
	 * @return The texture containing the sprite images.
	 */
	public Texture getSheet() {
		return sheet;
	}

	/**
	 * Gets the total number of sprites.
	 * 
	 * @return The total number of sprites.
	 */
	public int getNumSprites() {
		return spritesPerX * spritesPerY;
	}

	/**
	 * Gets the index of a particular pixel of a sprite in the sprite sheet
	 * texture.
	 * 
	 * @param spriteIndex
	 *            The index of the sprite in the sprite sheet.
	 * @param x
	 *            The location of the pixel in the sprite on X.
	 * @param y
	 *            The location of the pixel in the sprite on Y.
	 * @return The index of a pixel of a sprite in the sprite sheet texture.
	 */
	public int getPixelIndex(int spriteIndex, int x, int y) {
		return (getStartX(spriteIndex) + x + (getStartY(spriteIndex) + y)
				* sheet.getWidth());
	}

	/**
	 * Returns the starting location on X of a sprite in the sprite sheet
	 * texture.
	 * 
	 * @param index
	 *            The index of the sprite in the sprite sheet.
	 * @return The starting location on X of a sprite in the sprite sheet
	 *         texture.
	 */
	public int getStartX(int index) {
		Util.boundsAssert(index, 0, getNumSprites() - 1);
		return (index % spritesPerX) * borderedSpriteWidth + spriteBorderSize;
	}

	/**
	 * Returns the starting location on Y of a sprite in the sprite sheet
	 * texture.
	 * 
	 * @param index
	 *            The index of the sprite in the sprite sheet.
	 * @return The starting location on Y of a sprite in the sprite sheet
	 *         texture.
	 */
	public int getStartY(int index) {
		Util.boundsAssert(index, 0, getNumSprites() - 1);
		return ((index / spritesPerX) % spritesPerY) * borderedSpriteHeight
				+ spriteBorderSize;
	}

	private boolean rowHasOpaque(int y, int imgStartX, int imgEndX,
			ArrayBitmap pixels) {
		for (int x = imgStartX; x < imgEndX; x++) {
			if (pixels.isMoreOpaqueThanTransparent(x, y)) {
				return true;
			}
		}
		return false;
	}

	private boolean columnHasOpaque(int x, int imgStartY, int imgEndY,
			ArrayBitmap pixels) {
		for (int y = imgStartY; y < imgEndY; y++) {
			if (pixels.isMoreOpaqueThanTransparent(x, y)) {
				return true;
			}
		}
		return false;
	}

	private AABB generateAABB(int index, ArrayBitmap pixels) {
		int imgStartX = getStartX(index);
		int imgStartY = getStartY(index);
		int imgEndX = imgStartX + spriteWidth;
		int imgEndY = imgStartY + spriteHeight;
		int imgHeight = imgEndY - imgStartY;

		int minY = 0;
		int maxY = 0;
		int minX = 0;
		int maxX = 0;
		for (int j = imgStartY; j < imgEndY; j++) {
			if (rowHasOpaque(j, imgStartX, imgEndX, pixels)) {
				minY = j - imgStartY;
				break;
			}
		}
		for (int j = imgEndY - 1; j >= imgStartY; j--) {
			if (rowHasOpaque(j, imgStartX, imgEndX, pixels)) {
				maxY = j + 1 - imgStartY;
				break;
			}
		}

		int temp = imgHeight - maxY;
		maxY = imgHeight - minY;
		minY = temp;

		for (int i = imgStartX; i < imgEndX; i++) {
			if (columnHasOpaque(i, imgStartY, imgEndY, pixels)) {
				minX = i - imgStartX;
				break;
			}
		}
		for (int i = imgEndX - 1; i >= imgStartX; i--) {
			if (columnHasOpaque(i, imgStartY, imgEndY, pixels)) {
				maxX = i + 1 - imgStartX;
				break;
			}
		}
		return (new AABB(minX, minY, maxX, maxY)).move(-spriteWidth / 2.0,
				-spriteHeight / 2.0);
	}

	/**
	 * Returns an AABB that tightly bounds a particular sprite.
	 * 
	 * @param index
	 *            The index of the sprite in the sprite sheet.
	 * @param width
	 *            The scaled width of the AABB.
	 * @param height
	 *            The scaled height of the AABB.
	 * @return An AABB that tightly bounds a particular sprite.
	 */
	public AABB getAABB(int index, double width, double height) {
		double scaleX = width / spriteWidth;
		double scaleY = height / spriteHeight;
		return spriteAABBs[index].scale(scaleX, scaleY, 1.0);
	}
}
