package engine.rendering;

import engine.space.AABB;
import engine.util.Util;

public class SpriteSheet {
	private final Bitmap sheet;
	private final int spritesPerX;
	private final int spritesPerY;
	private final int spriteWidth;
	private final int spriteHeight;
	private final int borderedSpriteWidth;
	private final int borderedSpriteHeight;
	private final int spriteBorderSize;
	private final AABB[] spriteAABBs;

	public SpriteSheet(Bitmap spriteSheet, int spritesPerX, int spritesPerY, int spriteBorderSize) {
		this.sheet = spriteSheet;
		this.spritesPerX = spritesPerX;
		this.spritesPerY = spritesPerY;
		this.spriteBorderSize = spriteBorderSize;
		this.borderedSpriteWidth = spriteSheet.getWidth() / spritesPerX;
		this.borderedSpriteHeight = spriteSheet.getHeight() / spritesPerY;
		this.spriteWidth = borderedSpriteWidth - 2 * spriteBorderSize;
		this.spriteHeight = borderedSpriteHeight - 2 * spriteBorderSize;

		spriteAABBs = new AABB[getNumSprites()];
		int[] pixels = sheet.getPixels(null);
		for (int i = 0; i < getNumSprites(); i++) {
			spriteAABBs[i] = generateAABB(i, pixels);
		}
	}
	
	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}
	
	public double getSpriteAspect() {
		return (double)spriteWidth/(double)spriteHeight;
	}

	public Bitmap getSheet() {
		return sheet;
	}

	public int getNumSprites() {
		return spritesPerX * spritesPerY;
	}

	public int getPixelIndex(int spriteIndex, int x, int y) {
		return (getStartX(spriteIndex) + x + (getStartY(spriteIndex) + y)
				* sheet.getWidth());
	}

	public int getStartX(int index) {
		Util.boundsCheck(index, 0, getNumSprites() - 1);
		return (index % spritesPerX) * borderedSpriteWidth + spriteBorderSize;
	}

	public int getStartY(int index) {
		Util.boundsCheck(index, 0, getNumSprites() - 1);
		return ((index / spritesPerX) % spritesPerY) * borderedSpriteHeight + spriteBorderSize;
	}

	private boolean rowHasOpaque(int y, int imgStartX, int imgEndX, int[] pixels) {
		for (int x = imgStartX; x < imgEndX; x++) {
			if (pixels[x + y * sheet.getWidth()] < 0) {
				return true;
			}
		}
		return false;
	}

	private boolean columnHasOpaque(int x, int imgStartY, int imgEndY,
			int[] pixels) {
		for (int y = imgStartY; y < imgEndY; y++) {
			if (pixels[x + y * sheet.getWidth()] < 0) {
				return true;
			}
		}
		return false;
	}

	private AABB generateAABB(int index, int[] pixels) {
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

	public AABB getAABB(int index, double width, double height) {
		double scaleX = width / spriteWidth;
		double scaleY = height / spriteHeight;
		return spriteAABBs[index].scale(scaleX, scaleY, 1.0);
	}
}
