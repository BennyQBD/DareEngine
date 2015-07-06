/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.preprocessing;

import java.io.IOException;

import engine.rendering.ArrayBitmap;

/**
 * Generates borders on an existing sprite sheet with no borders.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SpriteSheetBorderMaker {
	/**
	 * Generates borders on an existing sprite sheet with no borders.
	 * 
	 * @param inputFileName
	 *            The name and path to the original sprite sheet with no
	 *            borders.
	 * @param outputFileName
	 *            The name and path to the desired output location.
	 * @param outputFormat
	 *            The image format to be used. For example, "png" and "jpg" are
	 *            valid values.
	 * @param spritesX
	 *            Number of sprites on the X axis.
	 * @param spritesY
	 *            Number of sprites on the Y axis.
	 * @param spriteSheetBorder
	 *            The number of pixels to border each sprite with on all sides.
	 * @throws IOException
	 */
	public static void generateBorder(String inputFileName,
			String outputFileName, String outputFormat, int spritesX,
			int spritesY, final int spriteSheetBorder) throws IOException {
		final ArrayBitmap srcImage = new ArrayBitmap(inputFileName);
		final int spriteWidth = srcImage.getWidth() / spritesX;
		final int spriteHeight = srcImage.getHeight() / spritesY;
		final int borderedSpriteWidth = spriteWidth + spriteSheetBorder * 2;
		final int borderedSpriteHeight = spriteHeight + spriteSheetBorder * 2;
		final ArrayBitmap destImage = new ArrayBitmap(borderedSpriteWidth
				* spritesX, borderedSpriteHeight * spritesY);

		destImage.visitAll(new ArrayBitmap.IVisitor() {
			@Override
			public void visit(int x, int y, int pixel) {
				int spriteNumX = x / borderedSpriteWidth;
				int spritePosX = x % borderedSpriteWidth;
				int spriteNumY = y / borderedSpriteHeight;
				int spritePosY = y % borderedSpriteHeight;

				boolean isLeftBorder = spritePosX < spriteSheetBorder;
				boolean isRightBorder = spritePosX >= borderedSpriteWidth
						- spriteSheetBorder;
				boolean isTopBorder = spritePosY < spriteSheetBorder;
				boolean isBottomBorder = spritePosY >= borderedSpriteHeight
						- spriteSheetBorder;
				int srcStartX = spriteNumX * spriteWidth;
				int srcStartY = spriteNumY * spriteHeight;
				int srcEndX = (spriteNumX + 1) * spriteWidth - 1;
				int srcEndY = (spriteNumY + 1) * spriteHeight - 1;
				int srcX = srcStartX + (spritePosX - spriteSheetBorder);
				int srcY = srcStartY + (spritePosY - spriteSheetBorder);

				if (isLeftBorder) {
					if (isTopBorder) {
						destImage.set(x, y, srcImage.get(srcStartX, srcStartY));
					} else if (isBottomBorder) {
						destImage.set(x, y, srcImage.get(srcStartX, srcEndY));
					} else {
						destImage.set(x, y, srcImage.get(srcStartX, srcY));
					}
				} else if (isRightBorder) {
					if (isTopBorder) {
						destImage.set(x, y, srcImage.get(srcEndX, srcStartY));
					} else if (isBottomBorder) {
						destImage.set(x, y, srcImage.get(srcEndX, srcEndY));
					} else {
						destImage.set(x, y, srcImage.get(srcEndX, srcY));
					}
				} else if (isTopBorder) {
					destImage.set(x, y, srcImage.get(srcX, srcStartY));
				} else if (isBottomBorder) {
					destImage.set(x, y, srcImage.get(srcX, srcEndY));
				} else {
					destImage.set(x, y, srcImage.get(srcX, srcY));
				}
			}
		});

		destImage.save(outputFileName, outputFormat);
	}
}
